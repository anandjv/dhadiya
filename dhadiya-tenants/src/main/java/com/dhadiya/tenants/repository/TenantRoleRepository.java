package com.dhadiya.tenants.repository;

import com.dhadiya.tenants.entity.TenantRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantRoleRepository extends JpaRepository<TenantRole, Long> {
    List<TenantRole> findByRealmCode(String realmCode);
    List<TenantRole> findByRealmCodeOrRealmCode(String realmCode1, String realmCode2);
    Optional<TenantRole> findByRoleCode(String roleCode);
    boolean existsByRoleCode(String roleCode);
}
