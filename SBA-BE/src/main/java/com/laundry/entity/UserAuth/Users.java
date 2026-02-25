package com.laundry.entity.UserAuth;

import com.laundry.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level =  AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted=false")
public class Users extends AbstractEntity<Long> implements UserDetails {

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(25)")
    String username;
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Roles> roles;

    String location;

    String latitude;

    String longitude;

    String avatar;

    @Column(name = "create_at", nullable = false, columnDefinition = "LocalDateTime default false")
    boolean isVip;

    @Column(name = "vip_expired_at", nullable = false, columnDefinition = "LocalDateTime default false")
    LocalDateTime vipExpiredAt;

    @Column(name = "email_verified", nullable = false, columnDefinition = "boolean default false")
    boolean emailVerified;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
