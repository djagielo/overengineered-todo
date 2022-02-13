package dev.bettercode.audit

import dev.bettercode.audit.infra.adapter.event.AuditSpringEventsListener
import dev.bettercode.audit.repository.AuditLog
import dev.bettercode.audit.repository.AuditRepository
import dev.bettercode.audit.service.AuditService
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.TransactionManager
import java.util.*
import javax.sql.DataSource


@Configuration
@EnableJpaRepositories(basePackages = ["dev.bettercode.audit.repository"])
@EntityScan(basePackageClasses = [AuditLog::class])
@Order(3)
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