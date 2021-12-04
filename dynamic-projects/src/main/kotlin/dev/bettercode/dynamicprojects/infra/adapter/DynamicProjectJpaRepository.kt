package dev.bettercode.dynamicprojects.infra.adapter

import dev.bettercode.dynamicprojects.query.DynamicProjectEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DynamicProjectJpaRepository : JpaRepository<DynamicProjectEntity, UUID>