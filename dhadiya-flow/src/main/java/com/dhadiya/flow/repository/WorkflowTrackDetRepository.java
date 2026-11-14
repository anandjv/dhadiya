package com.dhadiya.flow.repository;

import com.dhadiya.flow.entity.WorkflowTrackDet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowTrackDetRepository extends JpaRepository<WorkflowTrackDet, Long> {
    List<WorkflowTrackDet> findByCaseNumberOrderByPerformedOnAsc(String caseNumber);
    List<WorkflowTrackDet> findByCaseNumberOrderByPerformedOnDesc(String caseNumber);
}
