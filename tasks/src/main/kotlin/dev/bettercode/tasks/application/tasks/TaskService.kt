package dev.bettercode.tasks.application.tasks

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.TaskId
import dev.bettercode.tasks.application.projects.ProjectService
import dev.bettercode.tasks.domain.tasks.Task
import dev.bettercode.tasks.domain.tasks.TasksRepository

internal class TaskService(
    private val tasksRepository: TasksRepository,
    private val projectService: ProjectService
) {
    fun add(task: Task): Task {
        task.assignTo(projectService.getInboxProject())
        return tasksRepository.add(task)
    }

    fun addTaskForAProject(task: Task, projectId: ProjectId): Task? {
        return projectService.get(projectId)?.let {
            task.assignTo(it)
            return tasksRepository.save(task)
        }
    }

    fun delete(id: TaskId) {
        tasksRepository.delete(id)
    }
}