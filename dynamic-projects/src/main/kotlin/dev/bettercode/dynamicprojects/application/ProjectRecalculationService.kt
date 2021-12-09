package dev.bettercode.dynamicprojects.application

import dev.bettercode.dynamicprojects.DynamicProjectId
import dev.bettercode.dynamicprojects.domain.DynamicProjectRepository
import dev.bettercode.tasks.TaskDto
import org.springframework.data.domain.PageRequest

import org.springframework.data.domain.Pageable
import java.time.LocalDate
import java.util.function.Predicate

internal class ProjectRecalculationService(
    private val dynamicProjectRepository: DynamicProjectRepository,
    private val tasksQuery: TasksQueryService
) {
    fun recalculate(dynamicProjectId: DynamicProjectId) {
        dynamicProjectRepository.getProjectById(dynamicProjectId)?.let { project ->
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

            var page: Pageable = PageRequest.of(0, 100)
            var tasks = tasksQuery.getAllOpen(page)
            project.clearTasks()
            while (!tasks.isEmpty) {
                project.addTasks(tasks.filter(predicatesMap[project.name]!!).map { it.id }
                    .toSet())
                page = page.next()
                tasks = tasksQuery.getAllOpen(page)
            }

            dynamicProjectRepository.saveProject(project)
        }

    }
}
