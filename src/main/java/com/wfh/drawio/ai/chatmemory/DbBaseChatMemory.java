package com.wfh.drawio.ai.chatmemory;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfh.drawio.model.entity.Conversion;
import com.wfh.drawio.model.entity.Diagram;
import com.wfh.drawio.service.ConversionService;
import com.wfh.drawio.service.DiagramService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: DbBaseChatMemory
 * @Author wangfenghuan
 * @Package com.wfh.drawio.ai.chatmemory
 * @Date 2025/12/20 20:17
 * @description:
 */
@Component
@Slf4j
public class DbBaseChatMemory implements ChatMemory {
    
    
    @Resource
    private ConversionService conversionService;

    @Resource
    private DiagramService diagramService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void add(@NotNull String conversationId, @NotNull List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        saveConversation(conversationId, messages);
    }

    @Override
    public List<Message> get(String conversationId) {
        return get(conversationId, 10);
    }

    /**
     * 获取指定会话的最近 N 条消息（自定义扩展方法）
     */
    public List<Message> get(String conversationId, int lastN) {
        List<Message> messageList = getOrCreateConversation(conversationId);
        int fromIndex = Math.max(0, messageList.size() - lastN);
        return new ArrayList<>(messageList.subList(fromIndex, messageList.size()));
    }

    /**
     * 保存消息到当前会话id
     *
     * @param diagramId
     * @param messages
     */
    public void saveConversation(String diagramId, List<Message> messages) {
        Diagram diagram = diagramService.getOne(new QueryWrapper<>(Diagram.class).eq("id", Long.valueOf(diagramId)));
        Long userId = diagram.getUserId();
        for (Message message : messages) {
            // 获取当前消息的类型
            String type = message.getMessageType().getValue();
            Conversion conversion = new Conversion();
            conversion.setDiagramId(Long.valueOf(diagramId));
            String text = message.getText();
            conversion.setUserId(userId);
            if (type.equals(MessageType.ASSISTANT.getValue())){
                conversion.setMessageType("ai");
                conversion.setMessage(text);
            }else if (type.equals(MessageType.USER.getValue())){
                conversion.setMessage(text);
                conversion.setMessageType("user");
            }else {
                log.warn("未知消息类型");
            }
            // 插入一条消息记录
            conversionService.save(conversion);
        }
    }

    /**
     * 获取或创建会话
     *
     * @param conversationId
     * @return
     */
    public List<Message> getOrCreateConversation(String conversationId) {
        List<Conversion> diagrams = conversionService.getBaseMapper().selectList(new QueryWrapper<>(Conversion.class).eq("diagramId", conversationId));
        // 会话不为空，代表会话存在
        List<Message> messageList = new ArrayList<>();
        if (!diagrams.isEmpty()) {
            for (Conversion conversion : diagrams) {
                String messageType = conversion.getMessageType();
                String message = conversion.getMessage();
                if (message == null){
                    continue;
                }
                // 如果是用户消息
                if ("user".equals(messageType)){
                    messageList.add(new UserMessage(message));
                } else if ("ai".equals(messageType)) {
                    messageList.add(new AssistantMessage(message));
                }else {
                    log.warn("未知的消息类型");
                }
            }

        }
        return messageList;
    }

    /**
     * 清除指定会话的所有消息
     * @param conversationId
     */
    @Override
    public void clear(@NotNull String conversationId) {
        try {
            Long diagramId = Long.valueOf(conversationId);
            conversionService.getBaseMapper()
                    .delete(new QueryWrapper<Conversion>().eq("diagramId", diagramId));
        } catch (NumberFormatException e) {
            log.warn("无法清除会话，ID 非法: {}", conversationId);
        }
    }
}
