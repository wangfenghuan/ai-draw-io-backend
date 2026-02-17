# 🚀 Draw.io Backend (AI Enhanced)

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-green.svg)](https://spring.io/projects/spring-boot)
[![Node.js](https://img.shields.io/badge/Node.js-18+-339933.svg)](https://nodejs.org/)
[![Hocuspocus](https://img.shields.io/badge/Hocuspocus-2.x-blue.svg)](https://hocuspocus.dev/)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-Powered-blueviolet.svg)](https://spring.io/projects/spring-ai)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Docker Build](https://github.com/wangfenghuan/drawio-backend/actions/workflows/docker-build.yml/badge.svg)](https://github.com/wangfenghuan/drawio-backend/actions/workflows/docker-build.yml)

[English](#english) | [中文](#chinese)

---

<a name="english"></a>
## 🇬🇧 English

> A high-performance Draw.io backend service powered by Spring Boot 3 + Spring AI + Node.js. Supports real-time collaboration, AI-assisted drawing, and distributed architecture.

### Table of Contents
- [Introduction](#introduction)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [Quick Start](#quick-start)
- [Project Structure](#project-structure)
- [API Documentation](#api-documentation)

### Introduction
This project utilizes a **Spring Boot + Node.js** hybrid architecture to build next-generation intelligent drawing platforms:
- **Spring Boot Backend**: Handles core business logic, user management, file storage, AI conversation interfaces, and data persistence.
- **Node.js (Hocuspocus) Microservice**: Designed for **real-time collaboration**, based on WebSocket and Yjs CRDT algorithms, providing millisecond-level multi-user synchronization and persisting document snapshots back to Spring Boot.

### Key Features
#### 🤖 1. AI Assistance
- **Text-to-Diagram**: Generate flowcharts directly from natural language descriptions.
- **AI Editing**: Intelligently modify existing diagram structures and content.
- **Smart Completion**: AI automatically completes flowchart branches and nodes.
- **Streaming Response**: Typewriter effect similar to ChatGPT.

#### 🤝 2. Real-time Collaboration (Node.js)
- **High Performance Sync**: Custom Hocuspocus service handles high concurrency WebSocket connections.
- **CRDT Algorithms**: Uses Yjs to ensure eventual consistency of data during multi-user editing.
- **Incremental Updates**: Efficient binary differential synchronization.
- **Distributed Locks**: Combined with Redisson to ensure atomic business logic.

#### 🛡️ 3. Robust Architecture
- **Dual Authentication**: Node.js service validates user identity via internal interfaces with Spring Boot.
- **Data Persistence**: Collaborative content is automatically snapshotted and saved to MySQL.
- **Object Storage**: Integrated with MinIO/S3 for diagram file storage.

### Tech Stack
| Category | Technology | Description |
| --- | --- | --- |
| **Core (Java)** | Java 21, Spring Boot 3.5.9 | Core Business Backend |
| **Collab (Node)**| **Node.js, Hocuspocus, Yjs** | **Real-time Collaboration Microservice** |
| **AI** | Spring AI, OpenAI API | AI Capability Integration |
| **Database** | MySQL 8.0, MyBatis-Plus | Relational Database |
| **Cache & Msg** | Redis, Redisson | Caching, Distributed Locks |
| **Storage** | MinIO | Object Storage |
| **Security** | Spring Security | Security & Authentication |

### Quick Start
#### 1. Prerequisites
- **JDK**: 21+
- **Node.js**: 18+
- **Database**: MySQL 8.0+, Redis 6.0+
- **Storage**: MinIO

#### 2. Start Spring Boot Backend
Configure database and keys in `src/main/resources/application.yml` (or `application-local.yml` for local dev), then run:

```bash
# In the root directory
mvn clean package -DskipTests
java -jar target/drawio-backend-0.0.1-SNAPSHOT.jar
# Service runs at: http://localhost:8081
```

#### 3. Start Node.js Collaboration Service
This service handles WebSocket connections. Run it separately in the `node` directory:

```bash
cd node

# Install dependencies
npm install

# Start service
npm start
# Service runs at: http://localhost:1234
```

> **Note**: Ensure `SPRING_BOOT_URL` in `node/utils/api.js` or `.env` points to the correct Spring Boot address.

### Project Structure
```text
drawio-backend/
├── node/                # [NEW] Node.js Real-time Collaboration Microservice
│   ├── utils/           # Utilities (API calls)
│   ├── server.js        # Hocuspocus Server Entry
│   └── package.json     # Node Dependencies
├── src/main/java/       # Spring Boot Core Code
│   ├── controller/      # API Controllers
│   ├── service/         # Business Logic
│   ├── model/           # Data Models
│   ├── ai/              # Spring AI Module
│   └── ws/              # (Optional) Java WebSocket Logic
└── src/main/resources/  # Configuration Files
```

### API Documentation
- **API Docs**: [http://localhost:8081/api/doc.html](http://localhost:8081/api/doc.html)
- **WebSocket**: `ws://localhost:1234` (Provided by Node.js service)

---
<a name="chinese"></a>
## 🇨🇳 中文

> 基于 Spring Boot 3 + Spring AI + Node.js 的高性能 Draw.io 后端服务。支持实时协作、AI 辅助绘图和分布式架构。

### 目录
- [简介](#简介)
- [核心特性](#核心特性)
- [技术栈](#技术栈)
- [快速开始](#快速开始)
- [项目结构](#项目结构)
- [接口文档](#接口文档)

### 简介
本项目采用了 **Spring Boot + Node.js** 的双端混合架构，旨在构建下一代智能绘图平台：
- **Spring Boot 后端**: 负责核心业务逻辑、用户管理、文件存储、AI 对话接口以及数据持久化。
- **Node.js (Hocuspocus) 微服务**: 专为 **实时协作** 设计，基于 WebSocket 和 Yjs CRDT 算法，提供毫秒级的多人同步编辑体验，并负责将文档快照持久化回 Spring Boot。

### 核心特性
#### 🤖 1. AI 智能辅助
- **Text-to-Diagram**: 通过自然语言描述直接生成流程图。
- **AI 编辑**: 智能修改现有图表结构和内容。
- **智能续写**: AI 自动补充流程图分支和节点。
- **流式响应**: 类似 ChatGPT 的打字机效果。

#### 🤝 2. 实时多人协作 (Node.js)
- **高性能同步**: 定制的 Hocuspocus (Node.js) 服务处理高并发 WebSocket 连接。
- **CRDT 算法**: 使用 Yjs 确保多人编辑时的数据最终一致性。
- **增量更新**: 高效的二进制差异同步。
- **分布式锁**: 结合 Redisson 保证业务逻辑原子性。

#### 🛡️ 3. 完善的架构
- **双端鉴权**: Node.js 服务通过内部接口与 Spring Boot 验证用户身份。
- **数据回写**: 协作产生的内容会自动生成快照并保存至 MySQL。
- **对象存储**: 集成 MinIO/S3 存储图表文件。

### 技术栈
| 类别 | 技术 | 说明 |
| --- | --- | --- |
| **Core (Java)** | Java 21, Spring Boot 3.5.9 | 核心业务后端 |
| **Collab (Node)**| **Node.js, Hocuspocus, Yjs** | **实时协作微服务** |
| **AI** | Spring AI, OpenAI API | AI 能力接入 |
| **Database** | MySQL 8.0, MyBatis-Plus | 关系型数据库 |
| **Cache & Msg** | Redis, Redisson | 缓存、分布式锁 |
| **Storage** | MinIO | 对象存储 |
| **Security** | Spring Security | 安全认证 |

### 快速开始
#### 1. 环境准备
- **JDK**: 21+
- **Node.js**: 18+
- **Database**: MySQL 8.0+, Redis 6.0+
- **Storage**: MinIO

#### 2. 启动 Spring Boot 后端
修改 `src/main/resources/application.yml` (或 `application-local.yml` 用于本地开发) 配置数据库和 Key，然后运行：

```bash
# 根目录下
mvn clean package -DskipTests
java -jar target/drawio-backend-0.0.1-SNAPSHOT.jar
# 服务运行在: http://localhost:8081
```

#### 3. 启动 Node.js 协作服务
该服务用于 WebSocket 连接，在此目录下单独运行：

```bash
cd node

# 安装依赖
npm install

# 启动服务
npm start
# 服务运行在: http://localhost:1234
```

> **注意**: 确保 `node/utils/api.js` 或 `.env` 中的 `SPRING_BOOT_URL` 指向正确的 Spring Boot 地址。

### 项目结构
```text
drawio-backend/
├── node/                # [NEW] Node.js 实时协作微服务
│   ├── utils/           # 工具类 (API调用)
│   ├── server.js        # Hocuspocus 服务器入口
│   └── package.json     # Node 依赖配置
├── src/main/java/       # Spring Boot 核心代码
│   ├── controller/      # API 接口
│   ├── service/         # 业务逻辑
│   ├── model/           # 数据模型
│   ├── ai/              # Spring AI 模块
│   └── ws/              # (可选) Java端 WebSocket 逻辑
└── src/main/resources/  # 配置文件
```

### 接口文档
- **API Docs**: [http://localhost:8081/api/doc.html](http://localhost:8081/api/doc.html)
- **WebSocket**: `ws://localhost:1234` (由 Node.js 服务提供)

---

## 📈 Star History

[![Star History Chart](https://api.star-history.com/svg?repos=wangfenghuan/drawio-backend&type=Date)](https://star-history.com/#wangfenghuan/drawio-backend&Date)

## 🤝 Contribution | 贡献
Welcome Pull Requests! Since this project involves multi-language services, please indicate whether you are modifying the Java or Node.js part when submitting.

欢迎提交 Pull Request！由于包含多语言服务，提交时请注明修改的是 Java 还是 Node.js 部分。

## 📄 License | 许可证
[MIT License](LICENSE)

---
**Author**: fenghuanwang
