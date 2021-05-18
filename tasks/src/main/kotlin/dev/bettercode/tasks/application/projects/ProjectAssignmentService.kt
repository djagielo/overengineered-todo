package dev.bettercode.tasks.application.projects;

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.TaskId
import dev.bettercode.tasks.domain.projects.ProjectRepository
import dev.bettercode.tasks.domain.tasks.TasksRepository


internal class ProjectAssignmentService(private val projectRepository: ProjectRepository,
                                        private val tasksRepository: TasksRepository) {
    fun assign(taskId: TaskId, projectId: ProjectId) {
        projectRepository.get(projectId)
            ?.let {
                tasksRepository.get(taskId)?.let {
                    it.assignTo(projectId)
                    tasksRepository.save(it)
                }
            }
    }
}
