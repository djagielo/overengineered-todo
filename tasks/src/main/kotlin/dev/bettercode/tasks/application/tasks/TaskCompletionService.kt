package dev.bettercode.tasks.application.tasks

import dev.bettercode.commons.events.AuditLogCommand
import dev.bettercode.tasks.TaskId
import dev.bettercode.tasks.domain.tasks.TasksRepository
import dev.bettercode.tasks.shared.DomainEventPublisher
import dev.bettercode.tasks.shared.DomainResult
import java.time.Clock
import java.time.Instant

internal class TaskCompletionService(
    private val tasksRepository: TasksRepository,
    private val eventPublisher: DomainEventPublisher
) {
    fun complete(id: TaskId, clock: Clock = Clock.systemDefaultZone()): DomainResult {
        val task = tasksRepository.get(id) ?: return DomainResult.failure("No task with given id: $id")

        val result = task.complete(Instant.now(clock))

        if (result.successful) {
            tasksRepository.save(task)
            eventPublisher.publish(TaskCompleted(id))
            eventPublisher.publish(AuditLogCommand(message = "Task with id=TaskId(uuid=${task.id.uuid}) has been completed"))
        }

        return result
    }

    fun reopen(id: TaskId, clock: Clock = Clock.systemDefaultZone()): DomainResult {
        val task = tasksRepository.get(id) ?: return DomainResult.failure("No task with given id: $id")

        val result = task.reopen(Instant.now(clock))

        if (result.successful) {
            tasksRepository.save(task)
            eventPublisher.publish(TaskReopened(task.id))
            eventPublisher.publish(AuditLogCommand(message = "Task with id=TaskId(uuid=${task.id.uuid}) has been reopened"))
        }

        return result
    }
}
