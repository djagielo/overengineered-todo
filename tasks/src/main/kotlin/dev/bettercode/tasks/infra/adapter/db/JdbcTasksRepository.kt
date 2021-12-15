package dev.bettercode.tasks.infra.adapter.db

import dev.bettercode.tasks.TaskId
import dev.bettercode.tasks.domain.tasks.Task
import dev.bettercode.tasks.domain.tasks.TaskSnapshot
import dev.bettercode.tasks.domain.tasks.TaskStatus
import dev.bettercode.tasks.domain.tasks.TasksRepository
import io.vavr.control.Try
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import java.util.*

internal class JdbcTasksRepository(private val jdbcTemplate: JdbcTemplate) : TasksRepository {
    override fun add(task: Task): Task {
        val snapshot = task.toSnapshot()
        jdbcTemplate.update(
            "INSERT INTO tasks (id, name, status, completion_date, project_id, due_date) values (?,?,?,?,?,?)",
            snapshot.id.toString(),
            snapshot.name,
            snapshot.status.toString(),
            snapshot.completionDate,
            snapshot.projectId.toString(),
            snapshot.dueDate
        )
        return task
    }

    override fun get(id: TaskId): Task? {
        val taskSnapshot = Try.of {
            jdbcTemplate.queryForObject(
                "select id, name, status, completion_date, project_id, due_date from tasks where id=?",
                mapRowToTask(),
                id.uuid.toString()
            )
        }.recover(EmptyResultDataAccessException::class.java) { null }.get()

        return taskSnapshot?.let { Task.fromSnapshot(it) }
    }

    private fun mapRowToTask() = RowMapper { rs, _ ->
        TaskSnapshot(
            id = UUID.fromString(rs.getString("id")),
            name = rs.getString("name"),
            status = TaskStatus.valueOf(rs.getString("status")),
            completionDate = rs.getTimestamp("completion_date")?.toInstant(),
            projectId = UUID.fromString(rs.getString("project_id")),
            dueDate = rs.getDate("due_date")?.toLocalDate()
        )
    }

    override fun save(task: Task): Task {
        val snapshot = task.toSnapshot()
        jdbcTemplate.update(
            "UPDATE tasks SET name=?, status=?, completion_date=?, project_id = ?, due_date=? where id=?",
            snapshot.name,
            snapshot.status.toString(),
            snapshot.completionDate,
            snapshot.projectId.toString(),
            snapshot.dueDate,
            snapshot.id.toString()
        )

        return task
    }

    override fun delete(id: TaskId) {
        jdbcTemplate.update(
            "DELETE FROM tasks where id=?", id.uuid.toString()
        )
    }
}