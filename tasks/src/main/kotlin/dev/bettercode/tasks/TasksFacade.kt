package dev.bettercode.tasks

import dev.bettercode.tasks.application.projects.ProjectAssignmentService
import dev.bettercode.tasks.application.projects.ProjectCompletionService
import dev.bettercode.tasks.application.projects.ProjectCrudService
import dev.bettercode.tasks.application.tasks.TaskCompletionService
import dev.bettercode.tasks.application.tasks.TaskCrudService
import dev.bettercode.tasks.domain.projects.Project
import dev.bettercode.tasks.domain.tasks.Task
import dev.bettercode.tasks.shared.DomainResult

class TasksFacade internal constructor(
    private val taskCrudService: TaskCrudService,
    private val taskCompletionService: TaskCompletionService,
    private val projectCrudService: ProjectCrudService,
    private val projectAssignmentService: ProjectAssignmentService,
    private val projectCompletionService: ProjectCompletionService,
    private val tasksQueryService: TasksQueryService
) {
    fun add(task: TaskDto): TaskDto {
        return TaskDto.from(taskCrudService.add(Task(id = task.id, name = task.name)))!!
    }

    fun delete(id: TaskId) {
        return taskCrudService.delete(id)
    }

    fun complete(id: TaskId) {
        taskCompletionService.complete(id)
    }

    fun complete(task: TaskDto) {
        this.complete(task.id)
    }

    fun get(id: TaskId): TaskDto? {
        return TaskDto.from(taskCrudService.get(id))
    }

    fun getAll(): List<TaskDto> {
        return taskCrudService.getAll().map { TaskDto.from(it)!! }
    }

    fun getAllCompleted(): List<TaskDto> {
        return taskCrudService.getAllCompleted().map { TaskDto.from(it)!! }
    }

    fun uncomplete(task: TaskDto) {
        this.uncomplete(task.id)
    }

    fun uncomplete(id: TaskId) {
        taskCompletionService.uncomplete(id)
    }

    fun getProject(projectId: ProjectId): ProjectDto? {
        return projectCrudService.get(projectId)?.let {
            ProjectDto.from(it)
        }
    }

    fun getProjects(): List<ProjectDto> {
        return projectCrudService.getAll().map { ProjectDto.from(it)!! }
    }

    fun addProject(project: ProjectDto): ProjectDto {
        return ProjectDto.from(projectCrudService.add(Project(name = project.name)))!!
    }

    fun deleteProject(project: ProjectDto) {
        deleteProject(project.id)
    }

    fun deleteProject(projectId: ProjectId) {
        projectCrudService.delete(projectId)
    }

    fun assignToProject(task: TaskDto, project: ProjectDto): DomainResult {
        return projectAssignmentService.assign(task.id, project.id)
    }

    fun assignToProject(task: TaskDto, projectId: ProjectId): DomainResult {
        return projectAssignmentService.assign(task.id, projectId)
    }

    fun getTasksForProject(project: ProjectDto): List<TaskDto> {
        return getTasksForProject(project.id)
    }

    fun getTasksForProject(projectId: ProjectId): List<TaskDto> {
        return projectCrudService.get(projectId)?.let {
            return tasksQueryService.findAllForProject(projectId).map {
                TaskDto.from(it)!!
            }
        } ?: listOf()
    }

    fun addToProject(task: TaskDto, project: ProjectDto): TaskDto {
        return TaskDto.from(
            taskCrudService.addTaskForAProject(
                Task(name = task.name, id = task.id), project.id
            )
        )!!
    }

    fun getInbox(): ProjectDto? {
        return ProjectDto.from(projectCrudService.getInboxProject())
    }

    fun completeProject(project: ProjectDto): DomainResult {
        return projectCompletionService.complete(project.id)
    }
}