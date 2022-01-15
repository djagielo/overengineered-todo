package dev.bettercode.tasks.infra.adapter.db

import org.hibernate.annotations.Type
import java.time.Instant
import java.time.LocalDate
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tasks")
class TaskEntity(
    @Id
    @Type(type = "uuid-char")
    val id: UUID = UUID.randomUUID(),
    val name: String? = null,
    @Type(type = "uuid-char")
    val projectId: UUID? = null,
    val completionDate: Instant? = null,
    val dueDate: LocalDate? = null
)