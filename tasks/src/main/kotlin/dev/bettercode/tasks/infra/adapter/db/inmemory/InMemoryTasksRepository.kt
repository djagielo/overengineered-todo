package dev.bettercode.tasks.infra.adapter.db.inmemory

import dev.bettercode.tasks.TaskId
import dev.bettercode.tasks.domain.tasks.Task
import dev.bettercode.tasks.domain.tasks.TasksRepository
import java.util.*
import java.util.concurrent.ConcurrentHashMap

internal class InMemoryTasksRepository : TasksRepository {
    private val db = ConcurrentHashMap<UUID, Task>()

    override fun add(task: Task): Task {
        db[task.id.uuid] = task
        return task
    }

    override fun get(id: TaskId): Task? {
        return db[id.uuid]
    }

    override fun save(task: Task): Task {
        db[task.id.uuid] = task
        return task
    }

    fun getAll(): List<Task> {
        return ArrayList(db.values)
    }

    override fun delete(id: TaskId) {
        db.remove(id.uuid)
    }
}