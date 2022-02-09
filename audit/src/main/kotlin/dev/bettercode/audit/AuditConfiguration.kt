package dev.bettercode.audit

import dev.bettercode.audit.infra.adapter.event.AuditSpringEventsListener
import dev.bettercode.audit.repository.AuditLog
import dev.bettercode.audit.repository.AuditRepository
import dev.bettercode.audit.service.AuditService
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableAsync

@Configuration
@EnableJpaRepositories(basePackages = ["dev.bettercode.audit.repository"])
@EntityScan(basePackageClasses = [AuditLog::class])
@EnableAsync
class AuditConfiguration {
    @Bean
    internal fun auditService(auditRepository: AuditRepository): AuditService {
        return AuditService(auditRepository)
    }

    @Bean
    internal fun auditSpringEventListener(auditService: AuditService): AuditSpringEventsListener {
        return AuditSpringEventsListener(auditService)
    }
}