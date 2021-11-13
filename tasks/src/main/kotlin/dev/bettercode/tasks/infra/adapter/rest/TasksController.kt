@file:Suppress("unused")

package dev.bettercode.tasks.infra.adapter.rest

import dev.bettercode.tasks.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
class TasksController(val tasksFacade: TasksFacade) {
    val logger: Logger = LoggerFactory.getLogger(TasksController::class.java)

    @GetMapping("/tasks/{id}")
    internal fun getById(@PathVariable id: UUID): ResponseEntity<TaskDto> {
        return tasksFacade.get(TaskId(id))?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping("/tasks")
    internal fun create(@RequestBody taskRequest: TaskRequest): ResponseEntity<TaskDto> {
        val taskDto = taskRequest.toTaskDto()
        val result = tasksFacade.add(taskDto)
        if (result.successful) {
            return ResponseEntity.created(URI.create("tasks/${taskDto.id.uuid}"))
                .body(taskDto)
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    internal fun delete(@PathVariable id: UUID) {
        tasksFacade.delete(TaskId(id))
    }

    @PutMapping("/tasks/{id}/status")
    internal fun complete(@PathVariable id: UUID, @RequestBody taskCompletionRequest: TaskCompletionRequest) {
        if (taskCompletionRequest.completed)
            tasksFacade.complete(TaskId(id))
        else
            tasksFacade.reopenTask(TaskId(id))
    }

    @GetMapping("/projects")
    internal fun getAllProjects(): Page<ProjectDto> {
        return tasksFacade.getProjects()
    }

    @PostMapping("/projects")
    internal fun createProject(@RequestBody projectDto: ProjectDto) {
        tasksFacade.addProject(projectDto)
    }

    @GetMapping("/projects/{id}")
    internal fun getProject(@PathVariable id: UUID): ResponseEntity<ProjectDto> {
        return tasksFacade.getProject(ProjectId(id))?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/projects/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    internal fun deleteProject(@PathVariable id: UUID) {
        tasksFacade.deleteProject(ProjectId(id))
    }

    @GetMapping("/projects/{id}/tasks")
    internal fun getTasksForProject(@PathVariable id: UUID): ResponseEntity<Page<TaskDto>> {
        return tasksFacade.getProject(ProjectId(id))?.let {
            ResponseEntity.ok(tasksFacade.getTasksForProject(PageRequest.of(0, 100), it.id))
        } ?: ResponseEntity.notFound().build()
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception) {
        logger.error(ex.message, ex)
    }
}