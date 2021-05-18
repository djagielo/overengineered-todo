package dev.bettercode.tasks;

import dev.bettercode.bettercode.tasks.TasksFixtures
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test;

internal class ProjectUseCases {

    private val tasksFacade: TasksFacade = TasksConfiguration.tasksFacade()

    @Test
    fun `create project`() {
        // given - empty system
        assertThat(tasksFacade.getProjects()).isEmpty()

        // when
        tasksFacade.addProject(ProjectDto("BLOG"))

        // then
        val projects = tasksFacade.getProjects()
        assertThat(projects).hasSize(1)
        assertThat(projects.map { it.name }).hasSameElementsAs(listOf("BLOG"))
    }

    @Test
    fun `delete project`() {
        // given - a project of name BLOG
        val aProject = tasksFacade.addProject(ProjectDto("BLOG"))

        // when
        tasksFacade.deleteProject(aProject)

        // then
        assertThat(tasksFacade.getProjects()).hasSize(0)
    }

    @Test
    fun `tasks get assigned to project`() {
        // given - a project of name BLOG
        val blogProject = tasksFacade.addProject(ProjectDto("BLOG"))
        // and - it has no tasks
        assertThat(tasksFacade.getTasksForProject(blogProject)).hasSize(0)
        // and - 5 tasks are created
        val tasks = TasksFixtures.aNoOfTasks(5)
        tasks.forEach { tasksFacade.add(it) }

        // when
        tasks.take(3)
            .forEach {
                tasksFacade.assignToProject(it, blogProject)
            }

        // then
        val blogTasks = tasksFacade.getTasksForProject(blogProject)
        assertThat(blogTasks).hasSize(3)
        assertThat(blogTasks.map { it.name }).hasSameElementsAs(tasks.take(3).map { it.name })
    }

    @Test
    fun `tasks get unassigned from a project`() {
        // given - a project of name BLOG
        val blogProject = tasksFacade.addProject(ProjectDto("BLOG"))
        // and - it has 5 tasks
        val tasks = TasksFixtures.aNoOfTasks(5)
        tasks.forEach { tasksFacade.addToProject(it, blogProject) }

        // when
        tasks.take(3)
            .forEach {
                tasksFacade.assignToProject(it, tasksFacade.getInbox()!!)
            }

        // then
        val blogTasks = tasksFacade.getTasksForProject(blogProject)
        assertThat(blogTasks).hasSize(2)
        assertThat(blogTasks.map { it.name }).hasSameElementsAs(tasks.drop(3).map { it.name })
    }

    @Test
    fun `tasks by default get created in INBOX project`() {
        // given - no projects
        assertThat((tasksFacade.getProjects())).isEmpty()
        // and 5 tasks created
        val tasks = TasksFixtures.aNoOfTasks(5)
        tasks.forEach { tasksFacade.add(it) }

        // when - asking for projects
        val projects = tasksFacade.getProjects()

        // then - it should have INBOX projects created
        assertThat(projects).hasSize(1)
        assertThat(projects.map { it.name }).containsExactly("INBOX")
        assertThat(tasksFacade.getTasksForProject(projects.first())).containsExactlyInAnyOrder(*tasks.toTypedArray())
    }

    @Test
    fun `should switch task assignment from INBOX to new project`() {
        // given - a task in Inbox project
        tasksFacade.add(TasksFixtures.aNoOfTasks(1).first())
        // and - new project
        val privProject = tasksFacade.addProject(ProjectDto(name = "PRIV"))

        // when - asking for projects
        tasksFacade.assignToProject(tasksFacade.getAll().first(), privProject)

        // then - there should be total of 2 projects
        val projects = tasksFacade.getProjects()
        assertThat(projects).hasSize(2)
        assertThat(projects.map { it.name }).containsExactlyInAnyOrder("INBOX", "PRIV")

        // and - INBOX project should have 0 tasks
        assertThat(tasksFacade.getTasksForProject(tasksFacade.getInbox()!!.id)).isEmpty()
        // and - PRIV project has 1 task
        assertThat(tasksFacade.getTasksForProject(privProject)).hasSize(1)
    }
}
