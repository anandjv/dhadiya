package com.dhadiya.flow.repository;

import com.dhadiya.flow.entity.DynamicFormTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DynamicFormTemplateRepository extends JpaRepository<DynamicFormTemplate, Long> {
    List<DynamicFormTemplate> findByRealmCode(String realmCode);
    Optional<DynamicFormTemplate> findByRealmCodeAndTemplateKey(String realmCode, String templateKey);
}
