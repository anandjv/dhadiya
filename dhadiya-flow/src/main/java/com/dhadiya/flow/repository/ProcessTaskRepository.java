package com.dhadiya.flow.repository;

import com.dhadiya.flow.entity.ProcessTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessTaskRepository extends JpaRepository<ProcessTask, Long> {
    List<ProcessTask> findByInstanceId(Long instanceId);
    List<ProcessTask> findByAssigneeAndStatus(String assignee, String status);
    List<ProcessTask> findByStatus(String status);
}
