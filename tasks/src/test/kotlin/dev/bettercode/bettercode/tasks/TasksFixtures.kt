package dev.bettercode.bettercode.tasks

import dev.bettercode.tasks.TaskDto
import org.apache.commons.lang3.RandomStringUtils

class TasksFixtures {

    companion object {
        fun aNoOfTasks(count: Int): List<TaskDto> {
            return (1..count).map {
                TaskDto(RandomStringUtils.randomAlphabetic(30))
            }.toList()
        }
    }
}
