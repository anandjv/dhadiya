package com.dhadiya.flow.repository;

import com.dhadiya.flow.entity.DynamicGridRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DynamicGridRecordRepository extends JpaRepository<DynamicGridRecord, Long> {
    Page<DynamicGridRecord> findByRealmCodeAndGridKey(String realmCode, String gridKey, Pageable pageable);
    List<DynamicGridRecord> findByRealmCodeAndGridKey(String realmCode, String gridKey);
    List<DynamicGridRecord> findByProcessTrackingNumber(String trackingNumber);
    Optional<DynamicGridRecord> findByProcessTrackingNumberAndGridKey(String trackingNumber, String gridKey);
}
