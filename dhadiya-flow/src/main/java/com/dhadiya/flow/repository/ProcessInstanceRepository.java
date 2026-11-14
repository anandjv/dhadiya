package com.dhadiya.flow.repository;

import com.dhadiya.flow.entity.ProcessInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessInstanceRepository extends JpaRepository<ProcessInstance, Long> {
    List<ProcessInstance> findByRealmCode(String realmCode);
    Optional<ProcessInstance> findByTrackingNumber(String trackingNumber);
}
