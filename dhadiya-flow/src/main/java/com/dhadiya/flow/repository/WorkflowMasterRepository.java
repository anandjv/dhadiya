package com.dhadiya.flow.repository;

import com.dhadiya.flow.entity.WorkflowMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowMasterRepository extends JpaRepository<WorkflowMaster, Long> {
    List<WorkflowMaster> findByRealmCode(String realmCode);
    Optional<WorkflowMaster> findByWorkflowCode(String workflowCode);
    Optional<WorkflowMaster> findByRealmCodeAndWorkflowCode(String realmCode, String workflowCode);
    List<WorkflowMaster> findByRealmCodeAndActiveTrue(String realmCode);
}
