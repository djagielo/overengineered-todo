package dev.bettercode.tasks.infra.adapter.db

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.util.*

@org.springframework.stereotype.Repository
interface TasksQueryRepository : Repository<TaskEntity, UUID> {

    fun findAllByProjectId(pageable: Pageable, uuid: UUID): Page<TaskEntity>

    @Query("select t from TaskEntity t where t.projectId= :projectId ")
    fun findAllOpenForProject(pageable: Pageable, @Param("projectId") uuid: UUID): Page<TaskEntity>

    @Query("select t from TaskEntity t where t.completionDate is not null")
    fun findAllCompleted(pageable: Pageable): Page<TaskEntity>

    @Query("select t from TaskEntity t where t.completionDate is null")
    fun findAllOpen(pageable: Pageable): Page<TaskEntity>

    @Query("select t from TaskEntity t where t.dueDate is null")
    fun findAllNoDueDate(pageable: Pageable): Page<TaskEntity>

    @Query("select t from TaskEntity t where t.dueDate= :dueDate")
    fun findAllWithDueDate(pageable: Pageable, @Param("dueDate") dueDate: LocalDate): Page<TaskEntity>
}