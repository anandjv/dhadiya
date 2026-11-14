package com.dhadiya.flow.repository;

import com.dhadiya.flow.entity.DynamicGridView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DynamicGridViewRepository extends JpaRepository<DynamicGridView, Long> {
    List<DynamicGridView> findByRealmCodeAndGridKey(String realmCode, String gridKey);
}
