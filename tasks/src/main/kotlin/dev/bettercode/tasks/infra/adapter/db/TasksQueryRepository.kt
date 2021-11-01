package dev.bettercode.tasks.infra.adapter.db

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.TaskDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.data.repository.query.Param
import java.util.*

@org.springframework.stereotype.Repository
interface TasksQueryRepository : Repository<TaskEntity, UUID> {
    fun findAllByProjectId(pageable: Pageable, projectId: ProjectId): Page<TaskDto>

    @Query("select t from TaskEntity t where t.projectId= :projectId ")
    fun findAllOpenForProject(pageRequest: Pageable, @Param("projectId") projectId: ProjectId): Page<TaskDto>

    @Query("from TaskEntity e where e.completionDate is not null")
    fun findAllCompleted(pageRequest: Pageable): Page<TaskDto>

    //fun findAllCompletedForProject(pageRequest: PageRequest, projectId: ProjectId): Page<TaskDto>
    //fun findAllCompletedForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto>
    //fun findAllOpenForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto>
    //fun findAllForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto>
}