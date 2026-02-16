package com.wfh.drawio.service;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.util.JdbcConstants;
import com.wfh.drawio.model.dto.codeparse.ColumnInfoDTO;
import com.wfh.drawio.model.dto.codeparse.SqlParseResultDTO;
import com.wfh.drawio.model.dto.codeparse.SqlRelationshipDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service for parsing SQL DDL and inferring relationships
 * Powered by Alibaba Druid
 * @author fenghuanwang
 */
@Service
@Slf4j
public class SqlParserService {

    /**
     * Parse SQL content and return structured metadata with inferred relationships
     *
     * @param sqlContent The complete SQL DDL script
     * @return List of parsed table results
     */
    public List<SqlParseResultDTO> parseSql(String sqlContent) {
        // 1. Parse SQL using Druid (MySQL dialect by default, most common)
        // formatOption is not needed for AST parsing
        List<SQLStatement> statements = SQLUtils.parseStatements(sqlContent, JdbcConstants.MYSQL);

        List<SqlParseResultDTO> tables = new ArrayList<>();
        Map<String, SqlParseResultDTO> tableMap = new HashMap<>();

        // 2. Extract Metadata (Tables, Columns)
        for (SQLStatement stmt : statements) {
            if (stmt instanceof SQLCreateTableStatement) {
                SQLCreateTableStatement createTable = (SQLCreateTableStatement) stmt;
                SqlParseResultDTO tableDTO = extractTableInfo(createTable);
                tables.add(tableDTO);
                // Store in map for faster lookup during inference (lowercase for case-insensitive matching)
                tableMap.put(cleanName(tableDTO.getTableName()).toLowerCase(), tableDTO);
            }
        }

        // 3. Semantic Inference (The "AI" Part)
        inferRelationships(tables, tableMap);

        return tables;
    }

    private SqlParseResultDTO extractTableInfo(SQLCreateTableStatement createTable) {
        SqlParseResultDTO dto = new SqlParseResultDTO();
        
        // Extract Table Name
        String tableName = cleanName(createTable.getTableName());
        dto.setTableName(tableName);

        // Extract Comment
        if (createTable.getComment() != null) {
            dto.setComment(cleanString(createTable.getComment().toString()));
        }

        // Extract Columns
        List<ColumnInfoDTO> columns = new ArrayList<>();
        for (SQLTableElement element : createTable.getTableElementList()) {
            if (element instanceof SQLColumnDefinition) {
                SQLColumnDefinition colDef = (SQLColumnDefinition) element;
                ColumnInfoDTO colDto = new ColumnInfoDTO();
                
                colDto.setName(cleanName(colDef.getName().getSimpleName()));
                colDto.setType(colDef.getDataType().getName());
                
                // Check for Primary Key
                if (colDef.isPrimaryKey()) {
                    colDto.setPrimaryKey(true);
                }
                
                // Comment
                if (colDef.getComment() != null) {
                    colDto.setComment(cleanString(colDef.getComment().toString()));
                }

                columns.add(colDto);
            }
            // Note: Primary Key can also be defined as a table constraint, 
            // but for simplicity we focus on column definitions first. 
            // Druid's visitor can be used for more complex extraction if needed.
        }
        
        // Handle Table Level Constraints for PK if not found in columns
        if (columns.stream().noneMatch(ColumnInfoDTO::isPrimaryKey)) {
             // Basic check for generic "id" column if no explicit PK found (heuristic)
             columns.stream()
                    .filter(c -> "id".equalsIgnoreCase(c.getName()))
                    .findFirst()
                    .ifPresent(c -> c.setPrimaryKey(true));
        }

        dto.setColumns(columns);
        return dto;
    }

    /**
     * Infer relationships based on Naming Conventions and Semantics
     */
    private void inferRelationships(List<SqlParseResultDTO> tables, Map<String, SqlParseResultDTO> tableMap) {
        for (SqlParseResultDTO sourceTable : tables) {
            List<SqlRelationshipDTO> relationships = new ArrayList<>();
            
            for (ColumnInfoDTO column : sourceTable.getColumns()) {
                // Skip Primary Keys (usually they are targets, not sources of FKs)
                if (column.isPrimaryKey()) continue;

                String colName = column.getName().toLowerCase();
                String colComment = column.getComment();
                
                // Strategy 1: Standard "_id" suffix (e.g., user_id -> user or users)
                if (colName.endsWith("_id")) {
                    String coreName = colName.substring(0, colName.length() - 3); // remove "_id"
                    
                    // Try exact match (e.g. "user" table)
                    if (tableMap.containsKey(coreName)) {
                        relationships.add(createRelationship(sourceTable.getTableName(), column.getName(), 
                                tableMap.get(coreName).getTableName(), "id", "Semantic: Name Match (_id)", 0.8));
                    } 
                    // Try plural match (e.g. "users" table)
                    else if (tableMap.containsKey(coreName + "s")) {
                        relationships.add(createRelationship(sourceTable.getTableName(), column.getName(), 
                                tableMap.get(coreName + "s").getTableName(), "id", "Semantic: Plural Name Match", 0.8));
                    }
                }
                
                // Strategy 2: Exact Match with another table's PK (e.g. "parent_code" -> parent.code)
                // Simplified: Check if column name matches another table name exactly (common in some legacy DBs)
                if (tableMap.containsKey(colName)) {
                     relationships.add(createRelationship(sourceTable.getTableName(), column.getName(), 
                                tableMap.get(colName).getTableName(), "id", "Semantic: Column matches Table Name", 0.6));
                }

                // Strategy 3: Comment Analysis (e.g., "关联用户表")
                if (colComment != null && !colComment.isEmpty()) {
                     for (String targetTableName : tableMap.keySet()) {
                         // Don't match self
                         if (targetTableName.equalsIgnoreCase(sourceTable.getTableName())) continue;
                         
                         // Check if comment contains table name (simple heuristic)
                         if (colComment.contains(targetTableName)) {
                              relationships.add(createRelationship(sourceTable.getTableName(), column.getName(), 
                                tableMap.get(targetTableName).getTableName(), "id", "Semantic: Comment Match", 0.5));
                              break; // Found one match in comment, stop to avoid noise
                         }
                     }
                }
            }
            sourceTable.setRelationships(relationships);
            if (!relationships.isEmpty()) {
                log.info("Inferred {} relationships for table {}", relationships.size(), sourceTable.getTableName());
            }
        }
    }

    private SqlRelationshipDTO createRelationship(String sourceTable, String sourceCol, 
                                                  String targetTable, String targetCol, 
                                                  String method, double confidence) {
        SqlRelationshipDTO rel = new SqlRelationshipDTO();
        rel.setSourceTable(sourceTable);
        rel.setSourceColumn(sourceCol);
        rel.setTargetTable(targetTable);
        rel.setTargetColumn(targetCol); // Assuming 'id' as default target PK for inference
        rel.setType("1:N"); // Default assumption for simple FK
        rel.setInferenceMethod(method);
        rel.setConfidence(confidence);
        return rel;
    }

    private String cleanName(String name) {
        if (name == null) return "";
        return name.replaceAll("`", "").replaceAll("\"", "").replaceAll("'", "").trim();
    }
    
    private String cleanString(String text) {
        if (text == null) return "";
        return text.replaceAll("^'|'$", "").trim();
    }
}
