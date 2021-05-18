package dev.bettercode.tasks.domain.projects

import dev.bettercode.tasks.ProjectId

interface ProjectRepository {
    fun add(project: Project): Project
    fun get(projectId: ProjectId): Project?
    fun getInboxProject(): Project?
    fun save(project: Project)
    fun delete(projectId: ProjectId)
    fun getAll(): List<Project>
}