package com.wfh.drawio.ws.handler;

import com.wfh.drawio.common.ErrorCode;
import com.wfh.drawio.exception.BusinessException;
import com.wfh.drawio.mapper.DiagramRoomMapper;
import com.wfh.drawio.mapper.RoomSnapshotsMapper;
import com.wfh.drawio.mapper.RoomUpdatesMapper;
import com.wfh.drawio.mapper.SpaceUserMapper;
import com.wfh.drawio.model.entity.DiagramRoom;
import com.wfh.drawio.model.entity.RoomSnapshots;
import com.wfh.drawio.model.entity.RoomUpdates;
import com.wfh.drawio.model.entity.Space;
import com.wfh.drawio.model.entity.SpaceUser;
import com.wfh.drawio.model.enums.AuthorityEnums;
import com.wfh.drawio.model.enums.SpaceTypeEnum;
import com.wfh.drawio.model.enums.SpaceRoleEnum;
import com.wfh.drawio.service.DiagramRoomService;
import com.wfh.drawio.service.SpaceService;
import com.wfh.drawio.ws.service.RoomUpdateBatchService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Title: YjsHandler
 * @Author wangfenghuan
 * @Package com.wfh.drawio.ws.handler
 * @Date 2025/12/27 14:19
 * @description:
 */
@Slf4j
@Component
public class YjsHandler extends BinaryWebSocketHandler {

    /**
     * 房间映射
     */
    private final Map<String, Set<WebSocketSession>> roomSession = new ConcurrentHashMap<>();

    /**
     * Yjs 操作码定义
     */
    private static final byte OP_SYNC = 0x00;        // 同步数据
    private static final byte OP_POINTER = 0x01;     // 鼠标移动 (Awareness)
    private static final byte OP_UPDATE = 0x02;      // Yjs 更新数据


    @Resource
    private RoomSnapshotsMapper roomSnapshotsMapper;

    @Resource
    private RoomUpdatesMapper roomUpdatesMapper;

    @Resource
    private RoomUpdateBatchService batchService;

    @Resource
    private DiagramRoomMapper diagramRoomMapper;

    @Resource
    private SpaceService spaceService;

    @Resource
    private SpaceUserMapper spaceUserMapper;

    /**
     * 连接建立之后
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        // 基础权限校验
        if (!hasPermission(session.getPrincipal(), AuthorityEnums.ROOM_DIAGRAM_VIEW.getValue())) {
            session.close(CloseStatus.POLICY_VIOLATION);
            log.warn("❌ 用户无 {} 权限", AuthorityEnums.ROOM_DIAGRAM_VIEW.getValue());
            return;
        }

        // 获取房间ID
        String roomName = getRoomName(session);
        Long roomId;
        try {
            roomId = Long.valueOf(roomName);
        } catch (NumberFormatException e) {
            session.close(CloseStatus.NOT_ACCEPTABLE);
            log.warn("❌ 房间ID格式错误: {}", roomName);
            return;
        }

        // 查询房间信息
        DiagramRoom room = diagramRoomMapper.selectById(roomId);
        if (room == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("房间不存在"));
            log.warn("❌ 房间不存在: {}", roomId);
            return;
        }

        // 校验房间权限
        Principal principal = session.getPrincipal();
        if (principal == null || !(principal instanceof Authentication)) {
            session.close(CloseStatus.POLICY_VIOLATION);
            log.warn("❌ 未登录用户尝试连接协作房间");
            return;
        }

        Authentication auth = (Authentication) principal;
        Object principalObj = auth.getPrincipal();
        if (!(principalObj instanceof com.wfh.drawio.model.entity.User)) {
            session.close(CloseStatus.POLICY_VIOLATION);
            log.warn("❌ 无法获取用户信息");
            return;
        }

        com.wfh.drawio.model.entity.User loginUser = (com.wfh.drawio.model.entity.User) principalObj;

        // 校验空间权限
        Long spaceId = room.getSpaceId();
        if (spaceId == null) {
            // 公共房间：仅房主或管理员可访问
            if (!room.getOwnerId().equals(loginUser.getId()) && !isAdmin(auth)) {
                session.close(CloseStatus.POLICY_VIOLATION.withReason("无权限访问此公共房间"));
                log.warn("❌ 用户 {} 无权限访问公共房间 {}", loginUser.getId(), roomId);
                return;
            }
        } else {
            // 查询空间信息
            Space space = spaceService.getById(spaceId);
            if (space == null) {
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("空间不存在"));
                log.warn("❌ 空间不存在: {}", spaceId);
                return;
            }

            if (SpaceTypeEnum.PRIVATE.getValue() == space.getSpaceType()) {
                // 私有空间：仅空间创建人
                if (!space.getUserId().equals(loginUser.getId()) && !isAdmin(auth)) {
                    session.close(CloseStatus.POLICY_VIOLATION.withReason("无权限访问私有空间"));
                    log.warn("❌ 用户 {} 无权限访问私有空间 {}", loginUser.getId(), spaceId);
                    return;
                }
            } else if (SpaceTypeEnum.TEAM.getValue() == space.getSpaceType()) {
                // 团队空间：查询 SpaceUser 表校验角色
                SpaceUser spaceUser = spaceUserMapper.selectOne(
                    new LambdaQueryWrapper<SpaceUser>()
                        .eq(SpaceUser::getSpaceId, spaceId)
                        .eq(SpaceUser::getUserId, loginUser.getId())
                );
                if (spaceUser == null && !isAdmin(auth)) {
                    session.close(CloseStatus.POLICY_VIOLATION.withReason("不是团队空间成员"));
                    log.warn("❌ 用户 {} 不是团队空间 {} 的成员", loginUser.getId(), spaceId);
                    return;
                }
            }
        }

        // 权限校验通过，加入房间管理
        roomSession.computeIfAbsent(roomName, k -> new CopyOnWriteArraySet<>()).add(session);

        log.info("✅ 用户加入协作房间: {}, 当前房间人数: {}", roomName, roomSession.get(roomName).size());

        // 从数据库重建历史
        RoomSnapshots roomSnapshots = roomSnapshotsMapper.selectLatestByRoom(roomName);
        long lastUpdatedId = 0;
        // 如果存在快照，先发送快照数据
        if (roomSnapshots != null) {
            if (roomSnapshots.getSnapshotData() != null) {
                // 发送快照时添加 OP_SYNC 前缀
                byte[] snapshotData = roomSnapshots.getSnapshotData();
                byte[] payload = new byte[1 + snapshotData.length];
                payload[0] = OP_SYNC;
                System.arraycopy(snapshotData, 0, payload, 1, snapshotData.length);
                session.sendMessage(new BinaryMessage(payload));
            }
            // 记录快照截止到的id，后面只查询比这个id更晚的增量
            lastUpdatedId = roomSnapshots.getLastUpdateId();
        }
        // 获取快照之后的增量数据
        List<RoomUpdates> roomUpdates = roomUpdatesMapper.selectByRoomAndIdAfter(roomName, lastUpdatedId);
        // 逐条发送增量
        if (roomUpdates != null) {
            for (RoomUpdates roomUpdate : roomUpdates) {
                // 发送增量时添加 OP_UPDATE 前缀
                byte[] updateData = roomUpdate.getUpdateData();
                byte[] payload = new byte[1 + updateData.length];
                payload[0] = OP_UPDATE;
                System.arraycopy(updateData, 0, payload, 1, updateData.length);
                session.sendMessage(new BinaryMessage(payload));
            }
        }
        log.info("用户加入，加载了 {} 个快照和 {} 条增量", roomSnapshots != null ? 1 : 0, roomUpdates.size());

        // 广播当前在线人数
        broadcastUserCount(roomName);
    }

    /**
     * 处理前端发送的二进制消息
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleBinaryMessage(@NotNull WebSocketSession session, BinaryMessage message) throws Exception {
        byte[] payload = message.getPayload().array();
        if (payload.length < 1) {
            log.warn("⚠️ 收到空消息");
            return;
        }

        // 读取第一个字节作为 OpCode
        byte opCode = payload[0];
        String roomName = getRoomName(session);

        // 获取用户权限
        Principal principal = session.getPrincipal();
        boolean canView = hasPermission(principal, AuthorityEnums.ROOM_DIAGRAM_VIEW.getValue());
        boolean canEdit = hasPermission(principal, AuthorityEnums.ROOM_DIAGRAM_EDIT.getValue());

        // 无查看权限直接断开
        if (!canView) {
            session.close();
            return;
        }

        log.info("收到消息，房间: {}, OpCode: 0x{}, 长度: {}, 来自: {}",
                roomName, String.format("%02X", opCode), payload.length, session.getId());

        switch (opCode) {
            case OP_POINTER -> {
                // 鼠标移动消息，直接广播不存储
                broadcastBinaryToOthers(roomName, payload, session.getId());
            }
            case OP_UPDATE -> {
                // Yjs 更新消息，需要存储并广播
                if (canEdit) {
                    // 去掉 OpCode，只存储纯 Yjs 更新数据
                    byte[] yjsUpdate = Arrays.copyOfRange(payload, 1, payload.length);

                    // 持久化更新数据
                    RoomUpdates roomUpdates = new RoomUpdates();
                    roomUpdates.setUpdateData(yjsUpdate);
                    try {
                        // 尝试转换为 Long，如果失败则使用原始 roomName
                        roomUpdates.setRoomId(Long.valueOf(roomName));
                    } catch (NumberFormatException e) {
                        log.warn("⚠️ 房间 ID {} 无法转换为 Long，使用字符串处理", roomName);
                        // 如果需要支持字符串 roomName，需要修改 RoomUpdates 实体
                        // 暂时跳过存储
                    }
                    batchService.addUpdate(roomUpdates);

                    // 广播给其他用户（带 OpCode）
                    broadcastBinaryToOthers(roomName, payload, session.getId());
                } else {
                    log.warn("⛔ 拦截无权编辑操作: user={}", principal != null ? principal.getName() : "anonymous");
                }
            }
            default -> {
                log.warn("⚠️ 未知 OpCode: 0x{}", String.format("%02X", opCode));
            }
        }
    }

    /**
     * 连接关闭之后
     * @param session
     * @param status
     */
    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
        String roomName = getRoomName(session);
        Set<WebSocketSession> sessions = roomSession.get(roomName);
        if (sessions != null) {
            sessions.remove(session);
            // 广播更新后的用户数
            broadcastUserCount(roomName);
            log.info("👋 用户离开协作房间: {}, 当前房间人数: {}", roomName, sessions.size());

            // 如果房间空了，可以选择清理内存中的 history (如果已持久化到数据库)
            if (sessions.isEmpty()) {
                roomSession.remove(roomName);
                log.info("🧹 房间 {} 已清空", roomName);
            }
        }
    }

    /**
     * 处理文本消息（用于用户数统计等 JSON 消息）
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 转发 JSON 消息（如用户数统计）
        String roomName = getRoomName(session);
        broadcastTextToOthers(roomName, message.getPayload(), session.getId());
    }

    /**
     * 广播二进制消息给房间内其他用户
     * @param roomName
     * @param payload
     * @param senderId
     */
    private void broadcastBinaryToOthers(String roomName, byte[] payload, String senderId) {
        Set<WebSocketSession> sessions = roomSession.get(roomName);
        if (sessions != null) {
            log.info("准备广播给房间: {} 的其他 {} 个用户", roomName, sessions.size() - 1);
            for (WebSocketSession webSocketSession : sessions) {
                // 排除自己，只发给别人
                if (webSocketSession.isOpen() && !webSocketSession.getId().equals(senderId)) {
                    try {
                        webSocketSession.sendMessage(new BinaryMessage(payload));
                        log.info("已广播给: {}", webSocketSession.getId());
                    } catch (IOException e) {
                        log.error("❌ 广播失败: {}", e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 广播文本消息给房间内其他用户
     * @param roomName
     * @param payload
     * @param senderId
     */
    private void broadcastTextToOthers(String roomName, String payload, String senderId) {
        Set<WebSocketSession> sessions = roomSession.get(roomName);
        if (sessions != null) {
            for (WebSocketSession webSocketSession : sessions) {
                if (webSocketSession.isOpen() && !webSocketSession.getId().equals(senderId)) {
                    try {
                        webSocketSession.sendMessage(new TextMessage(payload));
                    } catch (IOException e) {
                        log.error("❌ 发送文本消息失败: {}", e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 广播用户数变化
     * @param roomName
     */
    private void broadcastUserCount(String roomName) {
        Set<WebSocketSession> sessions = roomSession.get(roomName);
        if (sessions == null || sessions.isEmpty()) {
            log.debug("⏭️ 房间 {} 不存在或为空，跳过用户数广播", roomName);
            return;
        }
        int userCount = sessions.size();
        String jsonMessage = String.format("{\"type\":\"user_count\",\"count\":%d}", userCount);
        log.info("📊 广播用户数: 房间={}, 人数={}", roomName, userCount);
        int successCount = 0;
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(jsonMessage));
                    successCount++;
                } catch (IOException e) {
                    log.error("❌ 发送用户数失败: {}", e.getMessage());
                }
            }
        }
        log.info("📤 用户数消息已发送给 {} 人", successCount);
    }

    /**
     * 从URL中取出房间名
     * @param session
     * @return
     */
    private String getRoomName(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null) {
            return "default";
        }
        String path = uri.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    /**
     * 校验权限
     * @param principal
     * @param targetPerm
     * @return
     */
    private boolean hasPermission(Principal principal, String targetPerm) {
        // 未登录直接拒绝
        if (principal == null) {
            return false;
        }
        if (principal instanceof Authentication) {
            Authentication auth = (Authentication) principal;
            // 获取所有权限
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            if (authorities == null || authorities.isEmpty()) {
                return false;
            }
            // 遍历权限
            for (GrantedAuthority authority : authorities) {
                String myPerm = authority.getAuthority();
                if (AuthorityEnums.ADMIN.getValue().equals(myPerm)) {
                    // 超级管理员直接放行
                    return true;
                }
                if (myPerm.equals(targetPerm)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否为管理员
     * @param auth
     * @return
     */
    private boolean isAdmin(Authentication auth) {
        if (auth == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        if (authorities == null || authorities.isEmpty()) {
            return false;
        }
        for (GrantedAuthority authority : authorities) {
            if (AuthorityEnums.ADMIN.getValue().equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
