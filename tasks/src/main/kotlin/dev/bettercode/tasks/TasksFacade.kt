package dev.bettercode.tasks

import dev.bettercode.tasks.application.projects.ProjectAssignmentService
import dev.bettercode.tasks.application.projects.ProjectCompletionService
import dev.bettercode.tasks.application.projects.ProjectCrudService
import dev.bettercode.tasks.application.tasks.TaskCompletionService
import dev.bettercode.tasks.application.tasks.TaskCrudService
import dev.bettercode.tasks.domain.projects.Project
import dev.bettercode.tasks.domain.tasks.Task
import dev.bettercode.tasks.query.TasksQueryService
import dev.bettercode.tasks.shared.DomainResult
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

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

    fun reopenTask(task: TaskDto) {
        this.reopenTask(task.id)
    }

    fun reopenTask(id: TaskId) {
        taskCompletionService.reopen(id)
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

    fun getTasksForProject(pageRequest: PageRequest, project: ProjectDto): Page<TaskDto> {
        return getTasksForProject(pageRequest, project.id)
    }

    fun getTasksForProject(pageRequest: PageRequest, projectId: ProjectId): Page<TaskDto> {
        return projectCrudService.get(projectId)?.let {
            return tasksQueryService.findAllForProject(pageRequest, projectId)
        } ?: Page.empty()
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