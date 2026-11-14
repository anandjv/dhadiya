package com.dhadiya.tenants.repository;

import com.dhadiya.tenants.entity.TenantRealm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRealmRepository extends JpaRepository<TenantRealm, Long> {
    Optional<TenantRealm> findByRealmCode(String realmCode);
    Optional<TenantRealm> findByCustomDomain(String customDomain);
    boolean existsByRealmCode(String realmCode);
}
