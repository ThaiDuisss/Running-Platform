package com.laundry.entity;

import com.laundry.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted=false")
public class Roles extends AbstractEntity<Long> implements GrantedAuthority {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    RoleEnum roleName;

    @ManyToMany(mappedBy = "roles")
    List<Users> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    Set<Permissions> permissions;

    @Override
    public String getAuthority() {
        return "ROLE_" + roleName.name();
    }
}
