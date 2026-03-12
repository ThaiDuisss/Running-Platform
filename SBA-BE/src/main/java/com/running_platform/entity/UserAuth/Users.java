package com.running_platform.entity.UserAuth;

import com.running_platform.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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

    @Column(name = "full_name", columnDefinition = "VARCHAR(50)")
    String fullName;

    String latitude;

    String longitude;

    String avatar;

    @Column(name = "vip_expired_at")
    LocalDateTime vipExpiredAt;

    @Column(name = "email_verified", nullable = false, columnDefinition = "boolean default false")
    boolean emailVerified = false;

    String phoneNumber;

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
