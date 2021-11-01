package dev.bettercode.tasks.infra.adapter.db

import dev.bettercode.tasks.ProjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.Repository
import java.util.*

@org.springframework.stereotype.Repository
interface ProjectsQueryRepository : Repository<ProjectEntity, UUID> {
    fun findAll(pageable: Pageable): Page<ProjectEntity>
    fun findById(projectId: ProjectId): ProjectEntity?
}