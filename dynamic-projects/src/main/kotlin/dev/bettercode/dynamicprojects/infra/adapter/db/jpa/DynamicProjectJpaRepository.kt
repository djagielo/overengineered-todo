package dev.bettercode.dynamicprojects.infra.adapter.db.jpa

import dev.bettercode.dynamicprojects.query.db.DynamicProjectEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
internal interface DynamicProjectJpaRepository : JpaRepository<DynamicProjectEntity, UUID>