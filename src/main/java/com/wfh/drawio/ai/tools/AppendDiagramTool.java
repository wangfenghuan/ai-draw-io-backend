package com.wfh.drawio.ai.tools;

import com.wfh.drawio.ai.utils.DiagramContextUtil;
import com.wfh.drawio.model.entity.Diagram;
import com.wfh.drawio.service.ConversionService;
import com.wfh.drawio.service.DiagramService;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * @Title: AppendDiagramTool
 * @Author wangfenghuan
 * @Package com.wfh.drawio.ai.tools
 * @Date 2025/12/20 20:45
 * @description: 追加图表工具
 */
@Component
public class AppendDiagramTool {


    @Resource
    private DiagramService diagramService;

    public AppendDiagramTool() {}

    @Tool(description = """
        Continue generating diagram XML when previous create_diagram output was truncated due to length limits.

        WHEN TO USE: Only call this tool after create_diagram was truncated (you'll see an error about truncation).

        CRITICAL INSTRUCTIONS:
        1. Do NOT include any wrapper tags - just continue the mxCell elements
        2. Continue from EXACTLY where your previous output stopped
        3. Complete the remaining mxCell elements
        4. If still truncated, call append_diagram again with the next fragment
        """)
    public ToolResult<DiagramSchemas.AppendDiagramRequest, String> execute(
            @ToolParam(description = "The XML fragment to append") DiagramSchemas.AppendDiagramRequest request
    ) {
        try {
            // 判断是否绑定了作用域
            if (!DiagramContextUtil.CONVERSATION_ID.isBound()){
                return ToolResult.error("System Error: ScopedValue noe bound");
            }
            // 当前的图表ID
            String diagramId = DiagramContextUtil.CONVERSATION_ID.get();
            // 4. 【关键】在后端内部获取 currentXml
            Diagram diagram = diagramService.getById(diagramId);
            String currentXml = diagram.getDiagramCode();

            String xmlFragment = request.getXml();

            // Validation
            if (xmlFragment.contains("UPDATE") || xmlFragment.contains("cell_id") || xmlFragment.contains("operations")) {
                return ToolResult.error("Invalid fragment: contains edit operation markers");
            }

            // Validate XML fragment
            DrawioXmlProcessor.ValidationResult validation =
                    DrawioXmlProcessor.validateAndParseXml("<wrapper>" + xmlFragment + "</wrapper>");

            if (!validation.valid) {
                return ToolResult.error("XML fragment validation failed: " + validation.error);
            }

            // Append to current XML
            String appendedXml = currentXml + "\n" + xmlFragment;

            // 5. 【关键】把拼接好的结果保存到数据库中去
            diagram.setDiagramCode(appendedXml);
            diagramService.updateById(diagram);

            return ToolResult.success(
                    appendedXml,
                    "XML fragment appended successfully. Total cells: " + DrawioXmlProcessor.extractMxCells(appendedXml).size()
            );

        } catch (Exception e) {
            return ToolResult.error("Failed to append diagram: " + e.getMessage());
        }
    }


}
