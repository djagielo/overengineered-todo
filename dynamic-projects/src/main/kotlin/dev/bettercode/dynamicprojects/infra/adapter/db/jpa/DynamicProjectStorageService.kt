package dev.bettercode.dynamicprojects.infra.adapter.db.jpa;

import dev.bettercode.dynamicprojects.DynamicProjectId
import dev.bettercode.dynamicprojects.domain.DynamicProject
import dev.bettercode.dynamicprojects.domain.DynamicProjectRepository
import dev.bettercode.dynamicprojects.infra.adapter.db.inmemory.DynamicProjectEntityMapper
import org.springframework.data.repository.findByIdOrNull

internal class DynamicProjectStorageService(
    private val mapper: DynamicProjectEntityMapper,
    private val projectRepo: DynamicProjectJpaRepository
) : DynamicProjectRepository {
    override fun getDefaultProjects(): List<DynamicProject> {
        return projectRepo.findAll().map(mapper::fromEntity)
    }

    override fun getProjectById(id: DynamicProjectId): DynamicProject? {
        return projectRepo.findByIdOrNull(id.uuid)?.let(mapper::fromEntity)
    }

    override fun saveProject(project: DynamicProject) {
        projectRepo.save(mapper.toEntity(project))
    }
}
