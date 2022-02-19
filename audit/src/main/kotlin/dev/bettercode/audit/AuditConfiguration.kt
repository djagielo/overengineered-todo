package dev.bettercode.audit

import dev.bettercode.audit.infra.adapter.event.AuditSpringEventsListener
import dev.bettercode.audit.repository.AuditLog
import dev.bettercode.audit.repository.AuditRepository
import dev.bettercode.audit.service.AuditService
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter


@Configuration
@EnableJpaRepositories(basePackages = ["dev.bettercode.audit.repository"])
@EntityScan(basePackageClasses = [AuditLog::class])
@Order(3)
@EnableWebSecurity(debug = true)
class AuditConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http!!.authorizeRequests {
            it.antMatchers("/audit**").authenticated()
        }
            .oauth2ResourceServer().jwt()
    }

    @Bean
    internal fun auditService(auditRepository: AuditRepository): AuditService {
        return AuditService(auditRepository)
    }

    @Bean
    internal fun auditSpringEventListener(auditService: AuditService): AuditSpringEventsListener {
        return AuditSpringEventsListener(auditService)
    }
}