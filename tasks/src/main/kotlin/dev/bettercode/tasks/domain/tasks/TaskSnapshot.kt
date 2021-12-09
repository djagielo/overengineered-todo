package dev.bettercode.tasks.domain.tasks

import java.time.Instant
import java.time.LocalDate
import java.util.*

internal data class TaskSnapshot(
    val id: UUID,
    val name: String,
    val status: TaskStatus,
    val completionDate: Instant?,
    val projectId: UUID?,
    val dueDate: LocalDate?
)