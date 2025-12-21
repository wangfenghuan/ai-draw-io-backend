package com.wfh.drawio.ai.tools;

import com.wfh.drawio.ai.utils.DiagramContextUtil;
import com.wfh.drawio.model.entity.Diagram;
import com.wfh.drawio.service.DiagramService;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Title: EditDiagramTool
 * @Author wangfenghuan
 * @Package com.wfh.drawio.ai.tools
 * @Date 2025/12/20 20:44
 * @description: 编辑图表工具
 */
@Component
public class EditDiagramTool {

    public EditDiagramTool() {}

    @Resource
    private DiagramService diagramService;

    @Tool(name = "edit_diagram", description = """
        Edit the current diagram by ID-based operations (update/add/delete cells).

        Operations:
        - update: Replace an existing cell by its id. Provide cell_id and complete new_xml.
        - add: Add a new cell. Provide cell_id (new unique id) and new_xml.
        - delete: Remove a cell by its id. Only cell_id is needed.

        For update/add, new_xml must be a complete mxCell element including mxGeometry.

        ⚠️ JSON ESCAPING: Every " inside new_xml MUST be escaped as \\".
        Example: id=\\"5\\" value=\\"Label\\"
        """)
    public ToolResult<DiagramSchemas.EditDiagramRequest, String> execute(
            @ToolParam(description = "The list of operations to perform on the diagram")
            DiagramSchemas.EditDiagramRequest request
    ) {
        try {
            // 判断是否绑定了作用域
            if (!DiagramContextUtil.CONVERSATION_ID.isBound()){
                return ToolResult.error("System Error: ScopedValue noe bound");
            }
            String diagramId = DiagramContextUtil.CONVERSATION_ID.get();
            Diagram diagram = diagramService.getById(diagramId);
            String currentXml = diagram.getDiagramCode();

            List<DiagramSchemas.EditOperation> operations = request.getOperations();

            if (operations == null || operations.isEmpty()) {
                return ToolResult.error("Operations list cannot be empty");
            }

            // Validate each operation
            for (DiagramSchemas.EditOperation op : operations) {
                if (op.getType() == null || op.getCellId() == null) {
                    return ToolResult.error("Each operation must have type and cell_id");
                }
                boolean res = ("update".equals(op.getType()) || "add".equals(op.getType())) &&
                        (op.getNewXml() == null || op.getNewXml().trim().isEmpty());
                if (res) {
                    return ToolResult.error("new_xml is required for " + op.getType() + " operations");
                }
            }

            // Apply operations
            DrawioXmlProcessor.OperationResult result = DrawioXmlProcessor.applyOperations(currentXml, operations);

            if (!result.success) {
                return ToolResult.error("Failed to apply operations: " + String.join(", ", result.errors));
            }

            // 3. 操作成功后，保存新的 XML 到数据库
            diagram.setDiagramCode(result.resultXml);
            diagramService.save(diagram);
            return ToolResult.success(
                    result.resultXml,
                    "Edit operations applied successfully:\n" +
                            String.join("\n", result.appliedOperations) +
                            (result.errors.isEmpty() ? "" : "\nErrors: " + String.join(", ", result.errors))
            );

        } catch (Exception e) {
            return ToolResult.error("Failed to edit diagram: " + e.getMessage());
        }
    }


}
