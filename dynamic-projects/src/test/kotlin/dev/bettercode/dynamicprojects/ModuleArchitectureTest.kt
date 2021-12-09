package dev.bettercode.dynamicprojects

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.Architectures

@AnalyzeClasses(packagesOf = [ModuleArchitectureTest::class])
class ModuleArchitectureTest {
    @ArchTest
    fun onionArchitectureTest(importedClasses: JavaClasses) {
        Architectures.onionArchitecture()
            .domainModels("dev.bettercode.dynamicprojects.domain")
            .applicationServices("dev.bettercode.dynamicprojects.application")
            .adapter("rest", "dev.bettercode.dynamicprojects.infra.adapter.rest")
            .adapter("events", "dev.bettercode.dynamicprojects.infra.adapter.events")
            .adapter("jpa", "dev.bettercode.dynamicprojects.infra.adapter.db.jpa")
            .adapter("tasks", "dev.bettercode.dynamicprojects.infra.adapter.tasks")
    }

    @ArchTest
    fun layeredArchitectureForQuery(importedClasses: JavaClasses) {
        Architectures.layeredArchitecture()
            .layer("Service").definedBy("dev.bettercode.dynamicprojects.query.service")
            .layer("Persistence").definedBy("dev.bettercode.dynamicprojects.query.db")

    }
}