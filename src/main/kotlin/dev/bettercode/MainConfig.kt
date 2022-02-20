package dev.bettercode

import dev.bettercode.audit.AuditConfiguration
import dev.bettercode.dynamicprojects.DynamicProjectsConfiguration
import dev.bettercode.tasks.TasksConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(value = [MainSecurityConfig::class, TasksConfiguration::class, DynamicProjectsConfiguration::class, AuditConfiguration::class])
@Configuration
class MainConfig