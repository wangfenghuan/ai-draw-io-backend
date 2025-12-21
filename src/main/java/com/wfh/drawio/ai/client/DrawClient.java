package com.wfh.drawio.ai.client;

import com.wfh.drawio.ai.advisor.MyLoggerAdvisor;
import com.wfh.drawio.ai.chatmemory.DbBaseChatMemory;
import com.wfh.drawio.ai.utils.PromptUtil;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Title: DrawClient
 * @Author wangfenghuan
 * @Package com.wfh.drawio.ai.client
 * @Date 2025/12/20 19:54
 * @description: 画图ai客户端
 */
@Component
public class DrawClient {

    private final ChatClient chatClient;

    @Resource
    private ToolCallback[] allTools;

    @Value("${spring.ai.openai.chat.options.model}")
    private String model;

    public DrawClient(ChatModel openaiChatModel, DbBaseChatMemory dbBaseChatMemory){
        chatClient = ChatClient.builder(openaiChatModel)
                .defaultSystem(PromptUtil.getSystemPrompt(model, true))
                .defaultAdvisors(new MyLoggerAdvisor())
                // 基于MySQL的对话记忆
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(dbBaseChatMemory).build())
                .build();
    }

    /**
     * 基础对话
     * @param message
     * @return
     */
    public String doChat(String message, String diagramId){
        ChatResponse chatResponse = this.chatClient
                .prompt()
                .user(message)
                .toolCallbacks(allTools)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, diagramId))
                .call()
                .chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        return text;
    }

}
