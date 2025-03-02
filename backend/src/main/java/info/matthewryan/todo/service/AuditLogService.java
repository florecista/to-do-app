package info.matthewryan.todo.service;

import info.matthewryan.todo.model.AuditLog;
import info.matthewryan.todo.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logEvent(String username, String eventType, String details) {
        AuditLog log = new AuditLog(username, eventType, details);
        auditLogRepository.save(log);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

    public List<AuditLog> getLogsByUser(String username) {
        return auditLogRepository.findByUsername(username);
    }
}
