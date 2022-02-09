package dev.bettercode.dynamicprojects.query.db

import dev.bettercode.dynamicprojects.DynamicProjectId
import dev.bettercode.dynamicprojects.query.db.DynamicProjectEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.Repository
import java.util.*

@org.springframework.stereotype.Repository
internal interface DynamicProjectQueryRepository : Repository<DynamicProjectEntity, UUID> {
    fun findAll(pageable: Pageable): Page<DynamicProjectEntity>
    fun findById(id: DynamicProjectId): DynamicProjectEntity?
    fun findByName(name: String): DynamicProjectEntity?
}
