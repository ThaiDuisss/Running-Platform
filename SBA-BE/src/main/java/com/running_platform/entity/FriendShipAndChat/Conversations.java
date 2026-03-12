package com.running_platform.entity.FriendShipAndChat;

import com.running_platform.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted=false")
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = "conversation_hash"
))
public class Conversations extends AbstractEntity<Long> {
    @Column(length = 200)
    String title;

    String conversationHash;

    boolean isGroup;

    @OneToMany(fetch = FetchType.LAZY)
    List<ConversationParticipants> conversationParticipants;

}
