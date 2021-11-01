package dev.bettercode.tasks.infra.adapter.db

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "projects")
class ProjectEntity(
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String = ""
)
