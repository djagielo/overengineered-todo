package dev.bettercode.audit.service;

import dev.bettercode.audit.repository.AuditLog
import dev.bettercode.audit.repository.AuditRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

class AuditService(private val auditRepo: AuditRepository) {
    fun save(auditLog: AuditLog) {
        auditRepo.save(auditLog)
    }

    fun getAll(pageable: PageRequest = PageRequest.of(0, 100)): Page<AuditLogDto> {
        return auditRepo.findAll(pageable).map {
            AuditLogDto(id = it.id ?: 0, msg = it.msg ?: "")
        }
    }
}
