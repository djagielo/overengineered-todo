package dev.bettercode.tasks.infra.adapter.db

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.data.repository.query.Param
import java.util.*

@org.springframework.stereotype.Repository
interface TasksQueryRepository : Repository<TaskEntity, UUID> {

    fun findAllByProjectId(pageable: Pageable, uuid: UUID): Page<TaskEntity>

    @Query("select t from TaskEntity t where t.projectId= :projectId ")
    fun findAllOpenForProject(pageRequest: Pageable, @Param("projectId") uuid: UUID): Page<TaskEntity>

    @Query("select t from TaskEntity t where t.completionDate is not null")
    fun findAllCompleted(pageRequest: Pageable): Page<TaskEntity>

    //fun findAllCompletedForProject(pageRequest: PageRequest, projectId: ProjectId): Page<TaskDto>
    //fun findAllCompletedForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto>
    //fun findAllOpenForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto>
    //fun findAllForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto>
}