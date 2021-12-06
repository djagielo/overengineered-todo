package dev.bettercode.dynamicprojects.query

import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "dynamic_projects")
class DynamicProjectEntity(
    @Id
    @Type(type = "uuid-char")
    val id: UUID = UUID.randomUUID(),
    val name: String? = null,
    @ElementCollection(fetch = FetchType.EAGER)
    val tasks: Set<UUID> = emptySet()
)