package dev.bettercode.tasks

import java.time.ZonedDateTime

internal class Task(val name: String, val id: TaskId = TaskId()) {
    internal var completionDate: ZonedDateTime? = null
    private var status = TaskStatus.NEW

    override fun toString(): String {
        return "Task(name='$name', id=$id)"
    }

    fun complete() {
        status = TaskStatus.COMPLETED
        completionDate = ZonedDateTime.now()
    }

    fun isCompleted(): Boolean {
        return status == TaskStatus.COMPLETED
    }

    fun uncomplete() {
        if (status == TaskStatus.COMPLETED) {
            if (completionDate?.toLocalDate()?.equals(ZonedDateTime.now().toLocalDate()) == false) {
                // not the same day, cannot be uncompleted
            }

            status = TaskStatus.NEW
            completionDate = null
        }
    }
}