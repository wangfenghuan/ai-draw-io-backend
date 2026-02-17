package com.wfh.drawio.core.model;

import lombok.Data;
import java.util.List;

/**
 * 3. 完整的分析结果
 */
@Data
public class ProjectAnalysisResult {
    private List<ArchNode> nodes;
    private List<ArchRelationship> relationships;
}
