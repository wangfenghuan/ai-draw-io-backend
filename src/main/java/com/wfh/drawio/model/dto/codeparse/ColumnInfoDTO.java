package com.wfh.drawio.model.dto.codeparse;

import lombok.Data;

/**
 * Column information extracted from DDL
 */
@Data
public class ColumnInfoDTO {
    /**
     * Column Name
     */
    private String name;

    /**
     * Data Type (e.g., VARCHAR, BIGINT)
     */
    private String type;

    /**
     * Is Primary Key?
     */
    private boolean isPrimaryKey;

    /**
     * Column Comment
     */
    private String comment;
}
