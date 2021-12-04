package dev.bettercode.dynamicprojects.application

import dev.bettercode.dynamicprojects.infra.adapter.events.RecalculateProject
import dev.bettercode.dynamicprojects.query.DynamicProjectQueryService
import dev.bettercode.tasks.application.projects.ProjectCreated

internal class DynamicProjectHandlers(
    private val defaultDynamicProjectsService: DefaultDynamicProjectsService,
    private val projectRecalculationService: ProjectRecalculationService,
    private val queryService: DynamicProjectQueryService
) {
    fun handleProjectCreated(event: ProjectCreated) {
        this.defaultDynamicProjectsService.createDefaults()
    }

    fun recalculateProject(event: RecalculateProject) {
        this.projectRecalculationService.recalculate(event.dynamicProjectId)
    }

    fun recalculateAllProjects() {
        queryService.getAll().forEach {
            this.projectRecalculationService.recalculate(it.id)
        }
    }
}