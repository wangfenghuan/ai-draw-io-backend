package com.wfh.drawio.model.dto.codeparse;

import lombok.Data;

/**
 * Inferred relationship between tables
 */
@Data
public class SqlRelationshipDTO {
    /**
     * Source Table Name
     */
    private String sourceTable;

    /**
     * Target Table Name
     */
    private String targetTable;

    /**
     * Source Column Name (Foreign Key)
     */
    private String sourceColumn;

    /**
     * Target Column Name (Primary Key)
     */
    private String targetColumn;

    /**
     * Relationship Type: "1:1", "1:N", "N:M"
     */
    private String type;

    /**
     * Confidence Score (0.0 - 1.0)
     * 1.0 = Explicit Foreign Key
     * 0.8 = Semantic Name Match (e.g. user_id -> users.id)
     * 0.6 = Comment Hint
     */
    private double confidence;

    /**
     * Inference Method: "FK", "Semantic Name Matching", "Comment Analysis"
     */
    private String inferenceMethod;
}
