package dev.bettercode.dynamicprojects.application

import dev.bettercode.dynamicprojects.domain.DynamicProject
import dev.bettercode.dynamicprojects.domain.DynamicProjectRepository
import java.time.LocalDate

class DefaultDynamicProjectsService(private val dynamicProjectRepository: DynamicProjectRepository) {

    fun createDefaults() {
        val defaultProjects = dynamicProjectRepository.getDefaultProjects()
        if (defaultProjects.isEmpty()) {
            dynamicProjectRepository.saveProject(DynamicProject(name = "Overdue", predicate = {
                it.dueDate?.isBefore(
                    LocalDate.now()
                ) ?: false
            }))

            listOf("Completed today", "Overdue", "Today", "Completed last week").forEach {
                dynamicProjectRepository.saveProject(DynamicProject(name = it, predicate = { true }))
            }
        }
    }
}