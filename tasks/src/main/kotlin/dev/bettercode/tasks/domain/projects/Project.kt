package dev.bettercode.tasks.domain.projects

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.shared.DomainResult
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

internal open class Project(
    val name: String,
    val id: ProjectId = ProjectId(),
    private var status: ProjectStatus = ProjectStatus.NEW,
    private var completionDate: ZonedDateTime? = null
) {

    val completed get() = this.status == ProjectStatus.COMPLETED

    fun complete(): DomainResult {
        return complete(Instant.now())
    }

    internal fun complete(instant: Instant): DomainResult {
        status = ProjectStatus.COMPLETED
        completionDate = instant.atZone(ZoneId.systemDefault())
        return DomainResult.success()
    }

    fun reopen(): DomainResult {
        return reopen(Instant.now())
    }

    fun reopen(instant: Instant): DomainResult {
        if (status == ProjectStatus.COMPLETED) {
            if (completionDate?.toLocalDate()?.equals(LocalDate.ofInstant(instant, ZoneId.systemDefault())) == false) {
                return DomainResult.failure("Project can be reopened within the same day as completed")
            }
        }
        return DomainResult.success()
    }
}