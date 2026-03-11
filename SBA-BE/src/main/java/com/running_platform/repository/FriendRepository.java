package com.running_platform.repository;

import com.running_platform.entity.FriendShipAndChat.FriendShips;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository
        extends JpaRepository<FriendShips ,Long> {
}
