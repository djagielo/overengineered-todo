package dev.bettercode.tasks.infra.adapter.db;

import java.time.Instant
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tasks")
class TaskEntity(
        @Id
        val id: UUID = UUID.randomUUID(),
        val projectId: UUID? = null,
        val completionDate: Instant? = null
)