package dev.bettercode.tasks

import java.util.*
import java.util.concurrent.ConcurrentHashMap

internal class InMemoryTasksRepository: TasksRepository {
    private val db = ConcurrentHashMap<UUID, Task>()

    override fun add(task: Task): Task {
        db[task.id.uuid] = task
        return task
    }

    override fun get(id: TaskId): Task? {
        return db[id.uuid]
    }

    override fun getAll(): List<Task> {
        return ArrayList(db.values)
    }

    override fun delete(id: TaskId) {
        db.remove(id.uuid)
    }

    override fun getAllCompleted(): List<Task> {
        return db.values.filter { it.isCompleted() }.toList()
    }
}