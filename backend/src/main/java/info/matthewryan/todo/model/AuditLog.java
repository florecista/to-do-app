package info.matthewryan.todo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;
    private String eventType;
    private String details;
    private LocalDateTime timestamp;

    public AuditLog() {
        this.timestamp = LocalDateTime.now();
    }

    public AuditLog(String username, String eventType, String details) {
        this.username = username;
        this.eventType = eventType;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters
    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getEventType() { return eventType; }
    public String getDetails() { return details; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
