package dev.bettercode.tasks.infra.adapter.db

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.data.repository.query.Param
import java.util.*

@org.springframework.stereotype.Repository
interface ProjectsQueryRepository : Repository<ProjectEntity, UUID> {
    @Query("select p from ProjectEntity p")
    fun findAll(pageable: Pageable): Page<ProjectEntity>

    @Query("select p from ProjectEntity p where p.id= :projectId")
    fun findById(@Param("projectId") uuid: UUID): ProjectEntity?
}