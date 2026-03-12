package com.running_platform.entity.UserAuth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.running_platform.entity.AbstractEntity;
import com.running_platform.security.Oauth2.common.SecurityEnums;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
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
public class Users extends AbstractEntity<Long> {

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(25)")
    String username;

    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = true)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Roles> roles;

    String location;

    @Column( columnDefinition = "VARCHAR(50)")
    String fullName;

    String latitude;

    String longitude;


//    @Column(name = "registered_provider_name")
    @Enumerated(EnumType.STRING)
    private SecurityEnums.AuthProviderId registeredProviderName;

//    @Column(name = "registered_provider_id")
    private String registeredProviderId;

    String imageUrl;

    @Column
    LocalDateTime vipExpiredAt;

    @Column(nullable = false, columnDefinition = "boolean default false")
    boolean emailVerified = false;

    String phoneNumber;

    // Will be using same verificationCode and verificationCodeExpiresAt for both (email-verification and password reset)
//    @Column(name = "verification_code")
    private String verificationCode;

//    @Column(name = "verification_code_expires_at")
    private Instant verificationCodeExpiresAt;
}
