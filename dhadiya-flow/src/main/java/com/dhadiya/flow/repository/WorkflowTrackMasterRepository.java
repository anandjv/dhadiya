package com.dhadiya.flow.repository;

import com.dhadiya.flow.entity.WorkflowTrackMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowTrackMasterRepository extends JpaRepository<WorkflowTrackMaster, Long> {
    Optional<WorkflowTrackMaster> findByCaseNumber(String caseNumber);
    List<WorkflowTrackMaster> findByRealmCode(String realmCode);
    List<WorkflowTrackMaster> findByRealmCodeAndWorkflowCode(String realmCode, String workflowCode);
    List<WorkflowTrackMaster> findByInitiatedBy(String initiatedBy);
    Page<WorkflowTrackMaster> findByRealmCode(String realmCode, Pageable pageable);
}
