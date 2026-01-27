package com.laundry.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.io.Serializable;
import java.util.Date;

@Data
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
     @Temporal(TemporalType.TIMESTAMP)
    Date creatDate;

    @Column(name = "updateAt")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date updateDate;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

}
