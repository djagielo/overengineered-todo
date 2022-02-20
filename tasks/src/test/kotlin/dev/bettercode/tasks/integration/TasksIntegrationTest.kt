package dev.bettercode.tasks.integration

import dev.bettercode.bettercode.tasks.TasksFixtures
import dev.bettercode.tasks.TasksFacade
import dev.bettercode.tasks.TasksTestConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest

@SpringBootTest(classes = [TasksTestConfiguration::class])
class TasksIntegrationTest {

    @Autowired
    lateinit var tasksFacade: TasksFacade

    @Test
    fun `should create and get task`() {
        // given - a saved task
        val task = TasksFixtures.aNoOfTasks(1).first()
        tasksFacade.add(task)

        // when - getting all open tasks
        val tasks = tasksFacade.getAllOpen(PageRequest.of(0, 1))

        // then - the task should be marked as completed and completion date should be set
        assertThat(tasks).hasSize(1)
        assertThat(tasks.map { it.name }).containsExactlyInAnyOrder(task.name)
    }

}