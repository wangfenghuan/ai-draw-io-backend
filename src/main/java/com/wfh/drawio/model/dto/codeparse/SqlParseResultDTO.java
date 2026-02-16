package com.wfh.drawio.model.dto.codeparse;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Result of SQL parsing and semantic analysis
 */
@Data
public class SqlParseResultDTO {
    /**
     * Table name
     */
    private String tableName;

    /**
     * Table comment
     */
    private String comment;

    /**
     * Columns
     */
    private List<ColumnInfoDTO> columns = new ArrayList<>();

    /**
     * Inferred Relationships (Foreign Keys & Semantic)
     */
    private List<SqlRelationshipDTO> relationships = new ArrayList<>();
}
