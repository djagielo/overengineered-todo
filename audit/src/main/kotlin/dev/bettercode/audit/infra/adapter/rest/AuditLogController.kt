package dev.bettercode.audit.infra.adapter.rest;

import dev.bettercode.audit.service.AuditLogDto;
import dev.bettercode.audit.service.AuditService
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController;

@RestController
internal class AuditLogController(private val auditService: AuditService) {

    @GetMapping("/audit/logs")
    fun auditLogs(): ResponseEntity<Page<AuditLogDto>> {
        return ResponseEntity.ok(auditService.getAll())
    }
}
