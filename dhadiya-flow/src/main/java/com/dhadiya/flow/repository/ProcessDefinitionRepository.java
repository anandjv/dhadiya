package com.dhadiya.flow.repository;

import com.dhadiya.flow.entity.ProcessDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessDefinitionRepository extends JpaRepository<ProcessDefinition, Long> {
    List<ProcessDefinition> findByRealmCode(String realmCode);
    Optional<ProcessDefinition> findByRealmCodeAndProcessKey(String realmCode, String processKey);
}
