package dev.bettercode.audit.repository

import org.springframework.data.jpa.repository.JpaRepository

interface AuditRepository: JpaRepository<AuditLog, Long>