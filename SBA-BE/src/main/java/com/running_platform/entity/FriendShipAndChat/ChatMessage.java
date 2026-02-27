package com.running_platform.entity.FriendShipAndChat;

import com.running_platform.entity.AbstractEntity;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted=false")
public class ChatMessage extends AbstractEntity<Long> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    Users sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    Conversations conversation;

    @Lob
    String content;

    @Enumerated(EnumType.STRING)
    MessageType type;
}
