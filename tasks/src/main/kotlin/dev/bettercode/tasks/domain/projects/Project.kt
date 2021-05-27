package dev.bettercode.tasks.domain.projects

import dev.bettercode.tasks.ProjectId
import java.time.ZonedDateTime

internal open class Project(
    val name: String,
    val id: ProjectId = ProjectId(),
    private var status: ProjectStatus = ProjectStatus.NEW,
    private var completionDate: ZonedDateTime? = null
) {

    val completed get() = this.status == ProjectStatus.COMPLETED

    fun complete() {
        status = ProjectStatus.COMPLETED
        completionDate = ZonedDateTime.now()
    }
}
