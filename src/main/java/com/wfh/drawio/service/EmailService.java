package com.wfh.drawio.service;

/**
 * 邮件服务
 *
 * @author fenghuanwang
 */
public interface EmailService {

    /**
     * 发送简单文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String to, String subject, String content);
}
