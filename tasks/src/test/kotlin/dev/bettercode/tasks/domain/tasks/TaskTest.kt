package dev.bettercode.tasks.domain.tasks

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.stream.Stream

class TaskTest {

    @Test
    fun `should fail to set dueDate when task completed`() {
        // given a completed task
        val completedTask = Task(name = "completed task").apply {
            this.complete(Instant.now().minus(1, ChronoUnit.DAYS))
        }

        // when - trying to set due date
        val result = completedTask.dueTo(LocalDate.now())

        // then
        assertThat(result.failure).isTrue
        assertThat(result.reason).isEqualTo("Cannot change dueDate of completed task")
    }

    companion object {
        @JvmStatic
        private fun dueDateFromThePastSource(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(LocalDate.now().minus(1, ChronoUnit.DAYS)),
                Arguments.of(LocalDate.now().minus(1, ChronoUnit.MONTHS)),
                Arguments.of(LocalDate.now().minus(6, ChronoUnit.MONTHS)),
                Arguments.of(LocalDate.now().minus(1, ChronoUnit.YEARS))
            )
        }

        @JvmStatic
        private fun correctDueDateSource(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(LocalDate.now(), LocalDate.now()),
                Arguments.of(LocalDate.now(), LocalDate.now().plus(10, ChronoUnit.DAYS)),
                Arguments.of(LocalDate.now().plus(10, ChronoUnit.DAYS), LocalDate.now().plus(10, ChronoUnit.MONTHS)),
            )
        }
    }

    @MethodSource("dueDateFromThePastSource")
    @ParameterizedTest
    fun `should fail to set dueDate from the past`(dueTo: LocalDate) {
        // given a task
        val aTask = Task(name = "A task")

        // when - trying to set due date
        val result = aTask.dueTo(dueTo)

        // then
        assertThat(result.failure).isTrue
        assertThat(result.reason).isEqualTo("Cannot set past dueDate")
    }

    @MethodSource("correctDueDateSource")
    @ParameterizedTest
    fun `should set dueDate no matter if it was set before or not`(previousDueDate: LocalDate, nextDueDate: LocalDate) {
        // given a task
        val aTask = Task(name = "A task").apply {
            this.dueTo(previousDueDate)
        }

        // when - trying to set new due date
        val result = aTask.dueTo(nextDueDate)

        // then
        assertThat(result.successful).isTrue
        assertThat(aTask.dueDate).isEqualTo(nextDueDate)
    }
}