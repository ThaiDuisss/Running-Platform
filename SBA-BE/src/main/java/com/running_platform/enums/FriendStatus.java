package com.running_platform.enums;

public enum FriendStatus {
    PENDING,     // đã gửi lời mời, chờ phản hồi
    ACCEPTED,    // đã trở thành bạn
    REJECTED,    // lời mời bị từ chối
    CANCELLED,   // người gửi hủy lời mời
    BLOCKED      // một trong hai bên chặn
}
