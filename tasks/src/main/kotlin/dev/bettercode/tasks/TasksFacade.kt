package dev.bettercode.tasks

class TasksFacade internal constructor(
    private val taskCrudService: TaskCrudService,
    private val taskCompletionService: TaskCompletionService
) {
    fun add(task: TaskDto): TaskDto {
        return TaskDto.from(taskCrudService.add(Task(name = task.name, id = task.id)))!!
    }

    fun delete(id: TaskId) {
        return taskCrudService.delete(id)
    }

    fun complete(id: TaskId) {
        taskCompletionService.complete(id)
    }

    fun complete(task: TaskDto) {
        this.complete(task.id)
    }

    fun get(id: TaskId): TaskDto? {
        return TaskDto.from(taskCrudService.get(id))
    }

    fun getAll(): List<TaskDto> {
        return taskCrudService.getAll().map { TaskDto.from(it)!! }
    }

    fun getAllCompleted(): List<TaskDto> {
        return taskCrudService.getAllCompleted().map { TaskDto.from(it)!! }
    }

    fun uncomplete(task: TaskDto) {
        this.uncomplete(task.id)
    }

    fun uncomplete(id: TaskId) {
        this.taskCompletionService.uncomplete(id)
    }
}