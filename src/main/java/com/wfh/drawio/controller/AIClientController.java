package com.wfh.drawio.controller;

import com.wfh.drawio.ai.client.DrawClient;
import com.wfh.drawio.ai.utils.DiagramContextUtil;
import com.wfh.drawio.model.entity.User;
import com.wfh.drawio.service.DiagramService;
import com.wfh.drawio.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: AIClientController
 * @Author wangfenghuan
 * @Package com.wfh.drawio.controller
 * @Date 2025/12/20 20:05
 * @description:
 */
@RestController
@RequestMapping("/chat")
@Slf4j
public class AIClientController {

    @Resource
    private DrawClient drawClient;

    @Resource
    private DiagramService diagramService;

    @Resource
    private UserService userService;

    /**
     * (基础)图表生成对话
     * @param message
     * @param diagramId 图表id
     * @return
     */
    @PostMapping("/gen")
    public String doChat(String message, String diagramId){
        return ScopedValue.where(DiagramContextUtil.CONVERSATION_ID, String.valueOf(diagramId))
                .call(() -> drawClient.doChat(message, diagramId));
    }

}
