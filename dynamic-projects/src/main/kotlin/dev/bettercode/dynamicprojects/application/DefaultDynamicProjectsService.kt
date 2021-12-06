package dev.bettercode.dynamicprojects.application

import dev.bettercode.dynamicprojects.domain.DynamicProject
import dev.bettercode.dynamicprojects.domain.DynamicProjectRepository
import dev.bettercode.tasks.TaskDto
import java.time.LocalDate
import java.util.function.Predicate

class DefaultDynamicProjectsService(private val dynamicProjectRepository: DynamicProjectRepository) {

    fun createDefaults() {
        val defaultProjects = dynamicProjectRepository.getDefaultProjects()

        val predicatesMap = mapOf<String, Predicate<TaskDto>>(
            "Completed today" to Predicate {
                it.completionDate?.equals(
                    LocalDate.now()
                ) == true
            },
            "Overdue" to Predicate {
                it.dueDate?.isBefore(
                    LocalDate.now()
                ) ?: false
            },
            "Today" to Predicate {
                it.dueDate?.isEqual(
                    LocalDate.now()
                ) ?: false
            }
        )

        if (defaultProjects.isEmpty()) {
            predicatesMap.forEach {
                dynamicProjectRepository.saveProject(
                    DynamicProject(
                        name = it.key
                    )
                )
            }
        }
    }
}