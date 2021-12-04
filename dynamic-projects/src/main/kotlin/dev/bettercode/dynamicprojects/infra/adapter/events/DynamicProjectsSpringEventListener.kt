package dev.bettercode.dynamicprojects.infra.adapter.events

import dev.bettercode.dynamicprojects.application.DynamicProjectHandlers
import dev.bettercode.tasks.application.projects.ProjectCreated
import dev.bettercode.tasks.application.tasks.TaskCreated
import org.springframework.context.event.EventListener

internal class DynamicProjectsSpringEventListener(private val dynamicProjectHandlers: DynamicProjectHandlers) {
    @EventListener
    fun handleProjectCreated(event: ProjectCreated) {
        dynamicProjectHandlers.handleProjectCreated(event)
    }

    @EventListener
    fun handleRecalculateProject(event: RecalculateProject) {
        dynamicProjectHandlers.recalculateProject(event)
    }

    @EventListener
    fun handleRecalculateProject(event: TaskCreated) {
        dynamicProjectHandlers.recalculateAllProjects()
    }
}