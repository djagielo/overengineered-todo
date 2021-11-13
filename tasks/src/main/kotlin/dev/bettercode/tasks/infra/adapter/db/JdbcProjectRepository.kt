package dev.bettercode.tasks.infra.adapter.db

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.domain.projects.Inbox
import dev.bettercode.tasks.domain.projects.Project
import dev.bettercode.tasks.domain.projects.ProjectRepository
import io.vavr.control.Try
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import java.util.*

internal class JdbcProjectRepository(private val jdbcTemplate: JdbcTemplate) : ProjectRepository {
    override fun add(project: Project): Project {
        jdbcTemplate.update(
            "INSERT INTO projects (id, name) VALUES(?, ?)",
            project.id.uuid.toString(), project.name
        )
        return project
    }

    override fun get(projectId: ProjectId): Project? {
        return jdbcTemplate.queryForObject(
            "select id,name from projects where id=?",
            mapRowToProject(),
            arrayOf(projectId.uuid.toString())
        )
    }

    private fun mapRowToProject() = RowMapper { rs, _ ->
        Project(name = rs.getString("name"), id = ProjectId(UUID.fromString(rs.getString("id"))))
    }

    override fun getInboxProject(): Project? {
        return Try.of {
            jdbcTemplate.queryForObject(
                "select p.id,name from projects p join inboxes i on p.id=i.project_id where i.tenantId=0",
                mapRowToProject()
            )
        }.recover(EmptyResultDataAccessException::class.java) { null }.get()
    }

    override fun save(project: Project) {
        jdbcTemplate.update(
            "insert into projects(id,name) values (?,?);",
            project.id.uuid.toString(), project.name
        )
    }

    override fun delete(projectId: ProjectId) {
        jdbcTemplate.update("delete from projects where id = ?", projectId.uuid.toString())
    }

    override fun createInbox(): Inbox {
        Inbox().let {
            add(it)
            jdbcTemplate.update("insert into inboxes (project_id) values (?);", it.id.uuid.toString())
            return it
        }
    }
}