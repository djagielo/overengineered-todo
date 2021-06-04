package dev.bettercode.tasks

import dev.bettercode.tasks.application.projects.ProjectAssignmentService
import dev.bettercode.tasks.application.projects.ProjectCompletionService
import dev.bettercode.tasks.application.projects.ProjectService
import dev.bettercode.tasks.application.tasks.TaskCompletionService
import dev.bettercode.tasks.application.tasks.TaskService
import dev.bettercode.tasks.domain.projects.Project
import dev.bettercode.tasks.domain.tasks.Task
import dev.bettercode.tasks.query.ProjectsQueryService
import dev.bettercode.tasks.query.TasksQueryService
import dev.bettercode.tasks.shared.DomainResult
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

class TasksFacade internal constructor(
    private val taskService: TaskService,
    private val taskCompletionService: TaskCompletionService,
    private val projectService: ProjectService,
    private val projectAssignmentService: ProjectAssignmentService,
    private val projectCompletionService: ProjectCompletionService,
    private val tasksQueryService: TasksQueryService,
    private val projectsQueryService: ProjectsQueryService
) {
    fun add(task: TaskDto): TaskDto {
        return TaskDto.from(taskService.add(Task(id = task.id, name = task.name)))!!
    }

    fun delete(id: TaskId) {
        return taskService.delete(id)
    }

    fun complete(id: TaskId) {
        taskCompletionService.complete(id)
    }

    fun complete(task: TaskDto) {
        this.complete(task.id)
    }

    fun get(id: TaskId): TaskDto? {
        return tasksQueryService.findById(id)
    }

    fun getOpenInboxTasks(pageRequest: PageRequest = PageRequest.of(0, 100)): Page<TaskDto> {
        return tasksQueryService.findAllOpen(pageRequest, projectService.getInboxProject())
    }

    fun getAllCompleted(pageRequest: PageRequest = PageRequest.of(0, 100)): Page<TaskDto> {
        return tasksQueryService.findAllCompleted(pageRequest)
    }

    fun reopenTask(task: TaskDto) {
        this.reopenTask(task.id)
    }

    private fun reopenTask(id: TaskId) {
        taskCompletionService.reopen(id)
    }

    fun getProject(projectId: ProjectId): ProjectDto? {
        return projectsQueryService.findById(projectId)
    }

    fun getProjects(): Page<ProjectDto> {
        return projectsQueryService.getAll()
    }

    fun addProject(project: ProjectDto): ProjectDto {
        return ProjectDto.from(projectService.add(Project(name = project.name)))!!
    }

    fun deleteProject(project: ProjectDto) {
        deleteProject(project.id)
    }

    fun deleteProject(projectId: ProjectId) {
        projectService.delete(projectId)
    }

    fun assignToProject(task: TaskDto, project: ProjectDto): DomainResult {
        return projectAssignmentService.assign(task.id, project.id)
    }

    fun assignToProject(task: TaskDto, projectId: ProjectId): DomainResult {
        return projectAssignmentService.assign(task.id, projectId)
    }

    fun getTasksForProject(pageRequest: PageRequest = PageRequest.of(0, 100), project: ProjectDto): Page<TaskDto> {
        return getTasksForProject(pageRequest, project.id)
    }

    fun getTasksForProject(pageRequest: PageRequest = PageRequest.of(0, 100), projectId: ProjectId): Page<TaskDto> {
        return projectsQueryService.findById(projectId)?.let {
            return tasksQueryService.findAllForProject(pageRequest, projectId)
        } ?: Page.empty()
    }

    fun addToProject(task: TaskDto, project: ProjectDto): TaskDto {
        return TaskDto.from(
            taskService.addTaskForAProject(
                Task(name = task.name, id = task.id), project.id
            )
        )!!
    }

    fun getInbox(): ProjectDto? {
        return ProjectDto.from(projectService.getInboxProject())
    }

    fun completeProject(project: ProjectDto): DomainResult {
        return projectCompletionService.complete(project.id)
    }
}