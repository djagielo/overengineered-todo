package dev.bettercode.tasks

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.time.LocalDate

internal class TasksQueryService {
    fun findAll(pageRequest: PageRequest): Page<Task> {
        TODO()
    }

    fun findAllCompleted(): Page<Task> {
       TODO()
    }

    fun findCompletedForDate(date: LocalDate): Page<Task> {
        TODO()
    }

    fun findNonCompletedForDate(date: LocalDate): Page<Task> {
        TODO()
    }

    fun findAllForDate(date: LocalDate): Page<Task> {
        TODO()
    }
}