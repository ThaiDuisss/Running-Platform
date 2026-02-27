package com.running_platform.repository;

import com.running_platform.constant.RoleEnum;
import com.running_platform.entity.UserAuth.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    @Query("SELECT r FROM Roles r WHERE r.roleName IN :roleName")
    Set<Roles> findByRoleNameIn(@Param("roleName") Set<RoleEnum> names);

    Roles findByRoleName(RoleEnum roleName);
}
