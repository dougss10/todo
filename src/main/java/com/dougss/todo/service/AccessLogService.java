package com.dougss.todo.service;

import com.dougss.todo.model.AccessLog;
import com.dougss.todo.repository.AccessLogRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessLogService {

    final AccessLogRepository accessLogRepository;

    public AccessLogService(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public AccessLog save(AccessLog accessLog) {
        return accessLogRepository.save(accessLog);
    }
}
