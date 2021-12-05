package dev.bettercode.tasks.domain.projects

import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.stream.Stream


internal class ProjectTest {

    companion object {
        @JvmStatic
        private fun reopenSource(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    LocalDateTime.of(2021, 5, 30, 12, 22).atZone(ZoneId.of("UTC")).toInstant(),
                    LocalDateTime.of(2021, 5, 30, 22, 13).atZone(ZoneId.of("UTC")).toInstant(),
                ),
                Arguments.of(
                    LocalDateTime.of(2021, 5, 30, 0, 0, 0, 123_000_000).atZone(ZoneId.of("UTC")).toInstant(),
                    LocalDateTime.of(2021, 5, 30, 23, 59, 59, 999_000_000).atZone(ZoneId.of("UTC"))
                        .toInstant()
                )
            )
        }

        @JvmStatic
        private fun reopenSourceFailure(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    LocalDateTime.of(2021, 5, 30, 23, 59).atZone(ZoneId.of("UTC")).toInstant(),
                    LocalDateTime.of(2021, 5, 31, 0, 1).atZone(ZoneId.of("UTC")).toInstant(),
                ),
                Arguments.of(
                    LocalDateTime.of(2021, 5, 25, 23, 59).atZone(ZoneId.of("UTC")).toInstant(),
                    LocalDateTime.of(2021, 5, 31, 0, 1).atZone(ZoneId.of("UTC")).toInstant(),
                )
            )
        }
    }


    @MethodSource("reopenSource")
    @ParameterizedTest
    fun `project can be reopen within the same day`(
        completionDate: Instant,
        reopenDate: Instant
    ) {
        // given
        val aProject = Project(name = RandomStringUtils.randomAlphabetic(5))
        // and project is completed
        aProject.complete(completionDate)

        // when
        val result = aProject.reopen(reopenDate)

        // then
        assertThat(result.successful).isTrue
    }

    @MethodSource("reopenSourceFailure")
    @ParameterizedTest
    fun `project reopen fails when not within the same day`(
        completionDate: Instant,
        reopenDate: Instant
    ) {
        // given
        val aProject = Project(name = RandomStringUtils.randomAlphabetic(5))
        // and project is completed
        aProject.complete(completionDate)

        // when
        val result = aProject.reopen(reopenDate)

        // then
        assertThat(result.successful).isFalse
        assertThat(result.reason).isEqualTo("Project can be reopened within the same day as completed")
    }
}