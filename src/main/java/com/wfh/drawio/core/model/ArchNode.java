package com.wfh.drawio.core.model;

import lombok.Data;
import java.util.List;

/**
 * 1. 架构图中的“节点” (比如一个类、一个表、一个微服务)
 */
@Data
public class ArchNode {
    private String id;          // 唯一标识 (e.g., "com.example.UserService")
    private String name;        // 显示名称 (e.g., "UserService")
    private String type;        // 类型 (e.g., "CLASS", "INTERFACE", "TABLE", "SERVICE")
    private String stereotype;  // 构造型/注解 (e.g., "@Controller", "@Repository")
    private List<String> fields; // 关键字段
    private List<String> methods; // 关键方法
}
