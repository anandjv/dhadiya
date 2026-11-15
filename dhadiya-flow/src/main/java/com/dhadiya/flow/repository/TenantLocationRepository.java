package com.dhadiya.flow.repository;

import com.dhadiya.flow.entity.TenantLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantLocationRepository extends JpaRepository<TenantLocation, Long> {
    List<TenantLocation> findByRealmCode(String realmCode);
    Optional<TenantLocation> findByLocationCode(String locationCode);
    boolean existsByLocationCode(String locationCode);
}
