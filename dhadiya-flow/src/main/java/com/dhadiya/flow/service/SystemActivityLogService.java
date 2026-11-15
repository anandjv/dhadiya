package com.dhadiya.flow.service;

import com.dhadiya.flow.entity.SystemActivityLog;
import com.dhadiya.flow.repository.SystemActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemActivityLogService {

    private final SystemActivityLogRepository activityLogRepository;

    public List<SystemActivityLog> getLogs(String realmCode) {
        return (realmCode != null && !realmCode.isBlank())
                ? activityLogRepository.findTop100ByRealmCodeOrderByTimestampDesc(realmCode)
                : activityLogRepository.findTop100ByOrderByTimestampDesc();
    }

    public SystemActivityLog recordLog(SystemActivityLog log) {
        if (log.getTimestamp() == null) log.setTimestamp(LocalDateTime.now());
        return activityLogRepository.save(log);
    }
}
