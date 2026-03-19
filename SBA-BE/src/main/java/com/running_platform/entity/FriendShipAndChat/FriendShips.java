package com.running_platform.entity.FriendShipAndChat;

import com.running_platform.entity.AbstractEntity;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.FriendStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "is_deleted=false")
public class FriendShips extends AbstractEntity<Long> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    Users requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addressee_id", nullable = false)
    Users addressee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    FriendStatus status;
}
