package com.dhadiya.flow.repository;

import com.dhadiya.flow.entity.SystemActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemActivityLogRepository extends JpaRepository<SystemActivityLog, Long> {
    List<SystemActivityLog> findTop100ByRealmCodeOrderByTimestampDesc(String realmCode);
    List<SystemActivityLog> findTop100ByOrderByTimestampDesc();
}
