package dev.bettercode.tasks.domain.projects

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.shared.DomainResult
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

internal open class Project(
    val name: String,
    val id: ProjectId = ProjectId(),
    private var status: ProjectStatus = ProjectStatus.NEW,
    private var completionDate: Instant? = null
) {

    val completed get() = this.status == ProjectStatus.COMPLETED

    internal fun complete(instant: Instant = Instant.now()): DomainResult {
        status = ProjectStatus.COMPLETED
        completionDate = instant
        return DomainResult.success()
    }

    fun reopen(instant: Instant = Instant.now()): DomainResult {
        if (status == ProjectStatus.COMPLETED) {
            if (!LocalDate.ofInstant(completionDate, ZoneId.of("UTC"))
                    .equals(LocalDate.ofInstant(instant, ZoneId.of("UTC")))
            ) {
                return DomainResult.failure("Project can be reopened within the same day as completed")
            }
        }
        return DomainResult.success()
    }
}
