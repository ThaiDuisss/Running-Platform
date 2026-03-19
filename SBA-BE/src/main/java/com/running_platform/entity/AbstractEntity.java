package com.running_platform.entity;

import com.running_platform.entity.UserAuth.Users;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.io.Serializable;
import java.time.Instant;

@Data
@MappedSuperclass
public abstract class AbstractEntity<T extends Serializable> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    T id;

    @Nullable
    @ManyToOne
    @CreatedBy
    @JoinColumn(name = "created_by_id")
    Users createdBy;


    @Column(name = "createdAt")
    @CreationTimestamp
    Instant creatDate;

    @Column(name = "updateAt")
    @UpdateTimestamp
    Instant updateDate;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

}
