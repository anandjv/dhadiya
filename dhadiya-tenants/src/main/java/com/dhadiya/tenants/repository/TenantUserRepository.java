package com.dhadiya.tenants.repository;

import com.dhadiya.tenants.entity.TenantUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantUserRepository extends JpaRepository<TenantUser, Long> {
    List<TenantUser> findByRealmCode(String realmCode);
    List<TenantUser> findByRole(String role);
    Optional<TenantUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
