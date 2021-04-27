package dev.bettercode.tasks

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
class TasksController(val tasksFacade: TasksFacade) {

    @GetMapping("/tasks")
    internal fun getAll(): List<TaskDto> {
        return tasksFacade.getAll()
    }

    @GetMapping("/tasks/{id}")
    internal fun getById(@PathVariable id: UUID): ResponseEntity<TaskDto> {
        return tasksFacade.get(TaskId(id))?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping("/tasks")
    internal fun create(@RequestBody taskRequest: TaskRequest): ResponseEntity<TaskDto> {
        val createdTask = tasksFacade.add(taskRequest.toTaskDto())
        return ResponseEntity.created(URI.create("tasks/${createdTask.id.uuid}"))
            .body(createdTask)
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    internal fun delete(@PathVariable id: UUID) {
        tasksFacade.delete(TaskId(id))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception) {
        println(ex)
    }
}