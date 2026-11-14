package com.dhadiya.flow.repository;

import com.dhadiya.flow.entity.WorkflowTaskItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowTaskItemRepository extends JpaRepository<WorkflowTaskItem, Long> {
    List<WorkflowTaskItem> findByAssignedToAndStatus(String assignedTo, String status);
    List<WorkflowTaskItem> findByRoleNameAndStatus(String roleName, String status);
    List<WorkflowTaskItem> findByStatus(String status);
    List<WorkflowTaskItem> findByCaseNumber(String caseNumber);
    Optional<WorkflowTaskItem> findByCaseNumberAndStageCodeAndStatus(String caseNumber, String stageCode, String status);
}
