package com.laundry.repository;

import com.laundry.entity.Roles;
import com.laundry.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    @Query("SELECT r FROM Roles r WHERE r.roleName IN :names")
    Set<Roles> findByRoleNameIn(@Param("names") Set<RoleEnum> names);
}
