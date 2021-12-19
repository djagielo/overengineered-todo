package dev.bettercode.dynamicprojects.infra.adapter.tasks

import dev.bettercode.dynamicprojects.application.TasksQueryService
import dev.bettercode.tasks.TaskDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.web.client.RestTemplate


private class TaskList(val content: List<TaskDto>)

class TasksRestAdapter(private val taskServiceUrl: String, private val restTemplate: RestTemplate) : TasksQueryService {
    override fun getAllOpen(pageable: Pageable): Page<TaskDto> {
        val result = restTemplate.getForEntity(
            "$taskServiceUrl/tasks?page=${pageable.pageNumber}&size=${pageable.pageSize}",
            TaskList::class.java
        )

        return PageImpl(result.body?.content ?: emptyList())
    }
}