package dev.bettercode.dynamicprojects.domain

import dev.bettercode.dynamicprojects.DynamicProjectId

interface DynamicProjectRepository {
    fun getDefaultProjects(): List<DynamicProject>
    fun getProjectById(id: DynamicProjectId): DynamicProject?
    fun saveProject(project: DynamicProject)
}