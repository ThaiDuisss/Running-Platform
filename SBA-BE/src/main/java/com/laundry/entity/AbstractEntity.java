package com.laundry.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class AbstractEntity<T extends Serializable> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    T id;

    @CreatedBy
    @Column(name = "createdBy", updatable = false)
    Long createdBy;


    @Column(name = "createdAt")
    @CreationTimestamp
    Instant createDate;

    @Column(name = "updateAt")
    @UpdateTimestamp
    Instant updateDate;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

}
