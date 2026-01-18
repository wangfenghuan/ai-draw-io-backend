# RoomController 权限修改示例

## 需要修改的关键方法

### 1. 创建房间 (/add)

```java
@PostMapping("/add")
@PreAuthorize("hasSpaceAuthority(#roomAddRequest.spaceId, 'space:diagram:add') or hasAuthority('admin')")
@Operation(summary = "创建房间")
public BaseResponse<Long> addRoom(@RequestBody RoomAddRequest roomAddRequest, HttpServletRequest request) {
    // ... 方法实现
}
```

### 2. 删除房间 (/delete)

```java
@PostMapping("/delete")
@PreAuthorize("hasRoomAuthority(#oldDiagramRoom.id, 'room:user:manage') or hasAuthority('admin')")
@Operation(summary = "删除房间")
public BaseResponse<Boolean> deleteDiagramRoom(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
    // ... 方法实现
}
```

### 3. 更新房间信息 (/update)

```java
@PostMapping("/update")
@PreAuthorize("hasRoomAuthority(#oldRoom.id, 'room:user:manage') or hasAuthority('admin')")
@Operation(summary = "更新房间信息")
public BaseResponse<Boolean> updateDiagramRoom(@RequestBody RoomUpdateRequest roomUpdateRequest, HttpServletRequest request) {
    // ... 方法实现
}
```

### 4. 查看房间详情 (/get/vo)

```java
@GetMapping("/get/vo")
@PreAuthorize("hasRoomAuthority(#id, 'room:diagram:view') or hasAuthority('admin')")
@Operation(summary = "查询房间详情")
public BaseResponse<DiagramRoomVO> getDiagramRoomVOById(long id, HttpServletRequest request) {
    // ... 方法实现
}
```

### 5. 编辑房间 (/edit)

```java
@PostMapping("/edit")
@PreAuthorize("hasRoomAuthority(#oldRoom.id, 'room:user:manage') or hasAuthority('admin')")
@Operation(summary = "编辑房间信息")
public BaseResponse<Boolean> editDiagramRoom(@RequestBody RoomEditRequest roomEditRequest, HttpServletRequest request) {
    // ... 方法实现
}
```

## WebSocket 处理器权限修改

### YjsHandler.java - afterConnectionEstablished 方法

在现有的权限检查基础上,添加细粒度的房间权限检查:

```java
@Override
public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
    // ... 现有的登录验证和空间权限检查代码 ...

    com.wfh.drawio.model.entity.User loginUser = (com.wfh.drawio.model.entity.User) principalObj;

    // 【新增】查询房间成员角色
    RoomMemberService roomMemberService = ApplicationContextProvider.getBean(RoomMemberService.class);
    RoomMember roomMember = roomMemberService.lambdaQuery()
            .eq(RoomMember::getRoomId, roomId)
            .eq(RoomMember::getUserId, loginUser.getId())
            .one();

    if (roomMember != null) {
        // 根据房间角色检查权限
        RoomRoleService roomRoleService = ApplicationContextProvider.getBean(RoomRoleService.class);

        // 检查查看权限
        if (!roomRoleService.hasAuthority(loginUser.getId(), roomId, AuthorityEnums.ROOM_DIAGRAM_VIEW.getValue())) {
            session.close(CloseStatus.POLICY_VIOLATION.withReason("无房间查看权限"));
            log.warn("❌ 用户 {} 无房间 {} 查看权限", loginUser.getId(), roomId);
            return;
        }

        log.info("✅ 用户 {} 加入房间 {} (角色: {})", loginUser.getId(), roomId, roomMember.getRoomRole());
    } else {
        // 如果房间没有设置角色成员,继续使用原来的逻辑(公共房间或空间成员检查)
        // ... 原有代码 ...
    }

    // ... 后续的房间加入逻辑 ...
}

@Override
protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
    // ... 获取 payload ...

    if (payload.length > 0 && payload[0] == OP_UPDATE) {
        // 【新增】编辑权限检查
        String roomName = getRoomName(session);
        Long roomId = Long.valueOf(roomName);

        Principal principal = session.getPrincipal();
        if (principal instanceof Authentication auth) {
            Object principalObj = auth.getPrincipal();
            if (principalObj instanceof com.wfh.drawio.model.entity.User loginUser) {
                // 检查编辑权限
                RoomRoleService roomRoleService = ApplicationContextProvider.getBean(RoomRoleService.class);
                if (!roomRoleService.hasAuthority(loginUser.getId(), roomId, AuthorityEnums.ROOM_DIAGRAM_EDIT.getValue())) {
                    log.warn("❌ 用户 {} 无房间 {} 编辑权限", loginUser.getId(), roomId);
                    return; // 不转发编辑消息
                }
            }
        }

        // ... 后续的消息处理逻辑 ...
    }
}
```

## 注意事项

1. **注入服务**: WebSocket Handler 不是 Spring Bean,需要通过 `ApplicationContextProvider` 获取服务
2. **权限粒度**: 房间权限的粒度比空间权限更细,支持到具体的房间
3. **兼容性**: 保持向后兼容,如果没有设置房间角色,仍然使用原有的权限检查逻辑
4. **性能考虑**: WebSocket 连接建立时缓存权限信息,避免每次消息都查询数据库

## ApplicationContextProvider 工具类

如果项目中没有,需要创建:

```java
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }
}
```
