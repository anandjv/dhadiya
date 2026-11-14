package com.dhadiya.flow.repository;

import com.dhadiya.flow.entity.DynamicGridDef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DynamicGridDefRepository extends JpaRepository<DynamicGridDef, Long> {
    List<DynamicGridDef> findByRealmCode(String realmCode);
    Optional<DynamicGridDef> findByRealmCodeAndGridKey(String realmCode, String gridKey);
    Optional<DynamicGridDef> findByGridKey(String gridKey);
    Optional<DynamicGridDef> findByProcessKey(String processKey);
}
