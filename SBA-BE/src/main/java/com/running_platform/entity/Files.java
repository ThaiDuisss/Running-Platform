package com.running_platform.entity;

import com.running_platform.entity.UserAuth.Users;
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
public class Files extends AbstractEntity<Long>{
    @Column(length = 500, nullable = false)
    String url;

    @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn()
    Users ownerId;
    @Lob
    String path;

    @Column(length = 100, nullable = false)
    String mime_type;

    Long size;

    String contentType;

    String md5CheckSum;
}
