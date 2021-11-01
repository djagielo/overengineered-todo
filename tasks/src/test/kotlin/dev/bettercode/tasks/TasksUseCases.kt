package dev.bettercode.tasks

import dev.bettercode.bettercode.tasks.TasksFixtures
import dev.bettercode.tasks.application.tasks.TaskCreated
import dev.bettercode.tasks.shared.InMemoryEventPublisher
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

internal class TasksUseCases {

    private val eventPublisher: InMemoryEventPublisher = InMemoryEventPublisher()
    private val tasksFacade: TasksFacade = TasksConfiguration.tasksFacade(eventPublisher)

    @Test
    fun `should be able to create and get task`() {
        // given
        val task = TaskDto("Do something")

        // when
        tasksFacade.add(task)

        // then - should get the task by id
        assertThat(tasksFacade.get(task.id)).isEqualTo(task)
        assertThat(eventPublisher.events).containsExactlyInAnyOrder(TaskCreated(task.id))
    }

    @Test
    fun `should be able to create multiple and get all the tasks`() {
        // given - 3 tasks to create
        val tasks = TasksFixtures.aNoOfTasks(3)

        // when - adding
        tasks.forEach { tasksFacade.add(it) }

        // then - should be able to retrieve them
        assertThat(tasksFacade.getOpenInboxTasks())
            .containsExactlyInAnyOrder(*tasks.toTypedArray())
        assertThat(eventPublisher.events).containsExactlyInAnyOrder(*tasks.map { TaskCreated(it.id) }.toTypedArray())
    }

    @Test
    fun `should be able to delete tasks`() {
        // given - 3 saved tasks
        val tasks = TasksFixtures.aNoOfTasks(3)
        tasks.forEach { tasksFacade.add(it) }

        // when - deleting one of them
        tasksFacade.delete(tasks.last().id)

        // then - the rest is still retrievable
        assertThat(tasksFacade.getOpenInboxTasks())
            .containsExactlyInAnyOrder(*tasks.take(2).toTypedArray())
    }

    @Test
    fun `should be able to mark task as completed`() {
        // given - a saved task
        val task = TasksFixtures.aNoOfTasks(1).first()
        tasksFacade.add(task)

        // when - marking task as completed
        tasksFacade.complete(task, Clock.systemDefaultZone())

        // then - the task should be marked as completed and completion date should be set
        val completedTask = tasksFacade.get(task.id)
        assertThat(completedTask?.completionDate).isCloseTo(Instant.now(), within(1, ChronoUnit.MINUTES))
    }

    @Test
    fun `undo task completion within the same day`() {
        // given - a saved task
        val task = TasksFixtures.aNoOfTasks(1).first()
        addAndComplete(task)

        // when - undoing task completion
        tasksFacade.reopenTask(task)

        // then - the task should be marked as completed and completion date should be set
        val result = tasksFacade.get(task.id)
        assertThat(result?.completionDate).isNull()
    }

    @Test
    fun `fail to undo task completion different day`() {
        // given - a saved task
        val task = TasksFixtures.aNoOfTasks(1).first()
        addAndComplete(task, Clock.fixed(Instant.now(), ZoneId.systemDefault()))

        // when - undoing task completion
        tasksFacade.reopenTask(task, Clock.fixed(Instant.now().plus(1, ChronoUnit.DAYS), ZoneId.systemDefault()))

        // then - the task should be marked as completed and completion date should be set
        val result = tasksFacade.get(task.id)
        assertThat(result?.completionDate).`as`("Completion date shouldn't be cleared").isNotNull
    }

    private fun addAndComplete(task: TaskDto, clock: Clock = Clock.systemDefaultZone()) {
        tasksFacade.add(task)
        tasksFacade.complete(task, clock)
    }

    @Test
    fun `get all completed tasks`() {
        // given - a saved task
        val tasks = TasksFixtures.aNoOfTasks(10)
        tasks.forEach { tasksFacade.add(it) }

        // when - marking task as completed
        val toComplete = listOf(tasks.first(), tasks.last(), tasks[5])
        toComplete.forEach(tasksFacade::complete)

        // then - the task should be marked as completed and completion date should be set
        val completedTasks = tasksFacade.getAllCompleted()
        assertThat(completedTasks.map { it.id })
            .containsExactlyInAnyOrder(*toComplete.map { it.id }.toTypedArray())
    }

    @Test
    fun `should not find a task with not existing id`() {
        // given - 3 saved tasks
        val tasks = TasksFixtures.aNoOfTasks(3)
        tasks.forEach { tasksFacade.add(it) }

        // when - asking for task with not existing id
        val task = tasksFacade.get(TaskId())

        // then
        assertThat(task).isNull()
    }
}