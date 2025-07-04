package com.example.gautoi.repository;

import com.example.gautoi.entity.AuditLogging;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditLoggingRepository extends JpaRepository<AuditLogging, UUID> {

}
