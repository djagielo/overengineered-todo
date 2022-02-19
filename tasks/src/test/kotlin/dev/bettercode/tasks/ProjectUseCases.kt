package dev.bettercode.tasks

import dev.bettercode.bettercode.tasks.TasksFixtures
import dev.bettercode.tasks.application.projects.ProjectCompleted
import dev.bettercode.tasks.application.projects.ProjectReopened
import dev.bettercode.tasks.application.projects.TaskAssignedToProject
import dev.bettercode.tasks.shared.InMemoryEventPublisher
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ProjectUseCases {

    private val inMemoryEventPublisher = InMemoryEventPublisher()
    private val tasksFacade: TasksFacade =
        TasksConfiguration.tasksFacade(inMemoryEventPublisher = inMemoryEventPublisher)

    @BeforeEach
    fun cleanUpEvents() {
        this.inMemoryEventPublisher.clear()
    }

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
        assertThat(tasksFacade.getTasksForProject(project = blogProject)).hasSize(0)
        // and - 5 tasks are created
        val tasks = TasksFixtures.aNoOfTasks(5)
        tasks.forEach { tasksFacade.add(it) }

        // when
        tasks.take(3)
            .forEach {
                assertThat(tasksFacade.assignToProject(it, blogProject).successful).isTrue()
            }

        // then
        val blogTasks = tasksFacade.getTasksForProject(project = blogProject)
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
                assertThat(tasksFacade.assignToProject(it, tasksFacade.getInbox()!!).successful).isTrue()
            }

        // then
        val blogTasks = tasksFacade.getTasksForProject(project = blogProject)
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
        assertThat(
            tasksFacade.getTasksForProject(
                project = projects.first()
            )
        ).containsExactlyInAnyOrder(*tasks.toTypedArray())
    }

    @Test
    fun `should switch task assignment from INBOX to new project`() {
        // given - a task in Inbox project
        val task = TasksFixtures.aNoOfTasks(1).first()
        tasksFacade.add(task)
        // and - new project
        val privProject = tasksFacade.addProject(ProjectDto(name = "PRIV"))

        // when - asking for projects
        tasksFacade.assignToProject(tasksFacade.getOpenInboxTasks().first(), privProject)

        // then - there should be total of 2 projects
        val projects = tasksFacade.getProjects()
        assertThat(projects).hasSize(2)
        assertThat(projects.map { it.name }).containsExactlyInAnyOrder("INBOX", "PRIV")

        // and - INBOX project should have 0 tasks
        assertThat(tasksFacade.getTasksForProject(projectId = tasksFacade.getInbox()!!.id)).isEmpty()
        // and - PRIV project has 1 task
        assertThat(tasksFacade.getTasksForProject(project = privProject)).hasSize(1)
        // and - assign event gets published
        assertThat(inMemoryEventPublisher.events).contains(
            TaskAssignedToProject(task.id, privProject.id)
        )
    }

    @Test
    fun `task assignment to non-existing project fails`() {
        // given - a task in Inbox project
        val task = TasksFixtures.aNoOfTasks(1).first()
        tasksFacade.add(task)

        // when - trying to assign task to non-existing project
        val result =
            tasksFacade.assignToProject(tasksFacade.getOpenInboxTasks().first(), ProjectId())

        // then - failure with proper reason should be returned
        assertThat(result.failure).isTrue
        assertThat(result.reason).isEqualTo("No project with given id")
    }

    @Test
    fun `task cannot be assigned to completed project`() {
        // given - a task in Inbox project
        val task = TasksFixtures.aNoOfTasks(1).first()
        tasksFacade.add(task)
        // and a project that is completed
        val project = tasksFacade.addProject(ProjectDto(name = "COMPLETED"))
        tasksFacade.completeProject(project)

        // when - trying to assign task to non-existing project
        val result = tasksFacade.assignToProject(tasksFacade.getOpenInboxTasks().first(), project)

        // then - failure with proper reason should be returned
        assertThat(result.failure).isTrue
        assertThat(result.reason).isEqualTo("Cannot assign to completed project")
    }

    @Test
    fun `completed task cannot be reassigned`() {
        // given - a task that's completed
        val task = TasksFixtures.aNoOfTasks(1).first()
        tasksFacade.add(task)
        tasksFacade.complete(task)
        // and a project
        val project = tasksFacade.addProject(ProjectDto("BLOG"))

        // when - trying to assign completed task to different project
        val result = tasksFacade.assignToProject(task, project)

        // then - failure with proper reason should be returned
        assertThat(result.failure).isTrue
        assertThat(result.reason).isEqualTo("Cannot assign completed task")
    }

    @Test
    fun `completed task needs to be reopened before reassignment`() {
        // given - a task that's completed
        val task = TasksFixtures.aNoOfTasks(1).first()
        tasksFacade.add(task)
        tasksFacade.complete(task)
        // and a project
        val project = tasksFacade.addProject(ProjectDto("BLOG"))
        // and a task gets reopened
        tasksFacade.reopenTask(task)

        // when - trying to assign reopened task to project
        val result = tasksFacade.assignToProject(task, project)

        // then - failure with proper reason should be returned
        assertThat(result.successful).isTrue
    }

    @Test
    fun `empty project can be completed`() {
        // given - an empty project to be completed
        val project = tasksFacade.addProject(ProjectDto("PROJECT TO BE COMPLETED"))

        // when - trying to complete it again
        val result = tasksFacade.completeProject(project)

        // then - should success and emit event
        assertThat(result.successful).isTrue
        assertThat(inMemoryEventPublisher.events).contains(ProjectCompleted(project.id))
    }

    @Test
    fun `completed project can be reopened at any time`() {
        // given - an empty project to be completed
        val project = tasksFacade.addProject(ProjectDto("PROJECT TO BE REOPENED"))
        tasksFacade.completeProject(project)

        // when - trying to complete it again
        val result = tasksFacade.reopenProject(project)

        // then - should success and emit event
        assertThat(result.successful).isTrue
        assertThat(inMemoryEventPublisher.events).contains(ProjectCompleted(project.id), ProjectReopened(project.id))
    }

    @Test
    fun `project that had been completed already, cannot be completed again`() {
        // given - a project that's completed
        val project = tasksFacade.addProject(ProjectDto("COMPLETED_PROJECT"))
        // and a task gets reopened
        tasksFacade.completeProject(project)

        // when - trying to complete it again
        val result = tasksFacade.completeProject(project)

        // then - failure with proper reason should be returned
        assertThat(result.successful).isFalse
        assertThat(result.reason).isEqualTo("Project is already completed")
    }


    // project deletion with forced flag deletes its tasks also

    // project deletion without forced flag moves open tasks to INBOX

    // project completion with special flag completes its tasks also

    // project completion without flag moves open tasks to INBOX
}
