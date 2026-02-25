package com.laundry.entity.FriendShipAndChat;

import com.laundry.entity.AbstractEntity;
import com.laundry.entity.UserAuth.Users;
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
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {"participant_id", "conversation_id"}
))
public class ConversationParticipants extends AbstractEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    Conversations conversation;

    boolean isAdmin;
}
