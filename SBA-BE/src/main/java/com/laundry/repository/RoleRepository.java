package com.laundry.repository;

import com.laundry.constant.RoleEnum;
import com.laundry.entity.UserAuth.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    @Query("SELECT r FROM Roles r WHERE r.roleName IN :roleName")
    Set<Roles> findByRoleNameIn(@Param("roleName") Set<RoleEnum> names);

    Roles findByRoleName(RoleEnum roleName);
    boolean existsByRoleName (RoleEnum roleName);
}
