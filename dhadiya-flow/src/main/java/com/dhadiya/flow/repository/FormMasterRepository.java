package com.dhadiya.flow.repository;

import com.dhadiya.flow.entity.FormMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormMasterRepository extends JpaRepository<FormMaster, Long> {
    List<FormMaster> findByRealmCode(String realmCode);
    Optional<FormMaster> findByFormCode(String formCode);
    Optional<FormMaster> findByRealmCodeAndFormCode(String realmCode, String formCode);
    List<FormMaster> findByRealmCodeAndStatus(String realmCode, String status);
}
