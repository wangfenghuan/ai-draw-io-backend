package com.wfh.drawio.controller;

import com.wfh.drawio.ai.client.DrawClient;
import com.wfh.drawio.ai.utils.DiagramContextUtil;
import com.wfh.drawio.common.ErrorCode;
import com.wfh.drawio.exception.BusinessException;
import com.wfh.drawio.model.entity.User;
import com.wfh.drawio.service.DiagramService;
import com.wfh.drawio.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

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

    /**
     * 流式生成图表
     * @param message
     * @param diagramId
     * @return
     */
    @PostMapping("/stream")
    public SseEmitter doChatStream(String message, String diagramId){
        // 使用线程安全的集合来收集响应内容
        CopyOnWriteArrayList<String> responseChunks = new CopyOnWriteArrayList<>();
        AtomicReference<String> fullResponse = new AtomicReference<>("");
        SseEmitter emitter = new SseEmitter();
        ScopedValue.where(DiagramContextUtil.CONVERSATION_ID, diagramId)
                .call(() -> drawClient.doChatStream(message, diagramId).subscribe(
                        chunk -> {
                            try {
                                // 收集每一个文本块
                                responseChunks.add(chunk);
                                fullResponse.updateAndGet(current -> current + chunk);

                                // 实时发送给客户端
                                emitter.send(SseEmitter.event()
                                        .data(chunk)
                                        .name("data"));
                            }catch (Exception e){
                                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "发送响应失败");
                            }
                        },
                        error -> {
                            try{
                                emitter.send(SseEmitter.event()
                                        .data("生成图表出错")
                                        .name("error"));
                                emitter.completeWithError(error);
                            }catch (Exception e){
                                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "发送响应失败");
                            }
                        },
                        emitter::complete
                ));
        return emitter;
    }

}
