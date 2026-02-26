package com.laundry.entity.UserAuth;

import com.laundry.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted=false")
public class Permissions extends AbstractEntity<Long> {
    String name;
    String description;

    @ManyToMany(mappedBy = "permissions")
    List<Roles> roles;
}
