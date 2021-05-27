package dev.bettercode.tasks.application.tasks

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.domain.tasks.Task
import dev.bettercode.tasks.TaskId
import dev.bettercode.tasks.application.projects.ProjectCrudService
import dev.bettercode.tasks.domain.tasks.TasksRepository

internal class TaskCrudService(
    private val tasksRepository: TasksRepository,
    private val projectCrudService: ProjectCrudService
) {
    fun add(task: Task): Task {
        task.assignTo(projectCrudService.getInboxProject())
        return tasksRepository.add(task)
    }

    fun addTaskForAProject(task: Task, projectId: ProjectId): Task? {
        return projectCrudService.get(projectId)?.let {
            task.assignTo(it)
            return tasksRepository.save(task)
        }
    }

    fun get(id: TaskId): Task? {
        return tasksRepository.get(id)
    }

    fun getAll(): List<Task> {
        return tasksRepository.getAll()
    }

    fun delete(id: TaskId) {
        tasksRepository.delete(id)
    }

    fun getAllCompleted(): List<Task> {
        return tasksRepository.getAllCompleted()
    }
}