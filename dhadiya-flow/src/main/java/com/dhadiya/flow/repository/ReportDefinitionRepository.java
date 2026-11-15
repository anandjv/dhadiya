package com.dhadiya.flow.repository;

import com.dhadiya.flow.entity.ReportDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportDefinitionRepository extends JpaRepository<ReportDefinition, Long> {
    List<ReportDefinition> findByRealmCode(String realmCode);
    Optional<ReportDefinition> findByReportKey(String reportKey);
}
