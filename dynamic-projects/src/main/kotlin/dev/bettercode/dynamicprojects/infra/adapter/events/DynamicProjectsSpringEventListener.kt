package dev.bettercode.dynamicprojects.infra.adapter.events

import dev.bettercode.dynamicprojects.application.DynamicProjectHandlers
import dev.bettercode.tasks.application.projects.ProjectCreated
import dev.bettercode.tasks.application.tasks.TaskCreated
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async

internal open class DynamicProjectsSpringEventListener(private val dynamicProjectHandlers: DynamicProjectHandlers) {
    @Suppress("UNUSED_PARAMETER")
    @EventListener
    @Async("eventsExecutor")
    open fun handleProjectCreated(event: ProjectCreated) {
        dynamicProjectHandlers.handleProjectCreated(event)
    }

    @EventListener
    @Async("eventsExecutor")
    open fun handleRecalculateProject(event: RecalculateProject) {
        dynamicProjectHandlers.recalculateProject(event)
    }

    @Suppress("UNUSED_PARAMETER")
    @EventListener
    @Async("eventsExecutor")
    open fun handleRecalculateProject(event: TaskCreated) {
        dynamicProjectHandlers.recalculateAllProjects()
    }
}