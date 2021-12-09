package dev.bettercode.dynamicprojects

import dev.bettercode.dynamicprojects.application.DynamicProjectHandlers
import dev.bettercode.dynamicprojects.infra.adapter.events.RecalculateProject
import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.TaskDto
import dev.bettercode.tasks.TasksFacade
import dev.bettercode.tasks.application.projects.ProjectCreated
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.AdditionalMatchers
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.mockito.kotlin.eq
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class DynamicProjectsSpec {

    private fun <T> notEq(value: T): T {
        return AdditionalMatchers.not(ArgumentMatchers.eq(value)) ?: value
    }

    private val dynamicProjectsFacade: DynamicProjectsFacade =
        DynamicProjectsConfiguration.dynamicProjectsFacade()

    private val tasksFacade: TasksFacade = mock(TasksFacade::class.java)

    private val dynamicProjectHandlers: DynamicProjectHandlers =
        DynamicProjectsConfiguration.dynamicProjectHandlers(tasksFacade)

    @Test
    fun `should create predefined dynamic-projects after first project gets created`() {
        // given - any project gets created
        dynamicProjectHandlers.handleProjectCreated(ProjectCreated(ProjectId()))

        // when
        val dynamicProjects = dynamicProjectsFacade.getProjects()

        // then
        assertThat(dynamicProjects.map { it.name }).containsExactlyInAnyOrder(
            "Completed today", "Today", "Overdue"
        )
    }

    @Test
    fun `should recalculate Overdue dynamic project membership`() {
        // given - any project to generate default ones
        dynamicProjectHandlers.handleProjectCreated(ProjectCreated(ProjectId()))
        // and - Overdue project created as a result
        val overdueProject = dynamicProjectsFacade.getProjectByName("Overdue")
        assertThat(overdueProject).isNotNull
        overdueProject!!

        // and - list of tasks
        val tasks = listOf(
            TaskDto(name = "task_due_tomorrow", dueDate = LocalDate.now().plusDays(1)),
            TaskDto(name = "task_due_in_2_days", dueDate = LocalDate.now().plusDays(2)),
            TaskDto(name = "task_due_in_a_month", dueDate = LocalDate.now().plusDays(30))
        )

        val tasksOverdue = listOf(
            TaskDto(name = "task_due_yesterday", dueDate = LocalDate.now().minusDays(1)),
            TaskDto(name = "task_due_2_days_ago", dueDate = LocalDate.now().minusDays(2)),
            TaskDto(name = "task_due_month_ago", dueDate = LocalDate.now().minusDays(30))
        )

        doReturn(Page.empty<TaskDto>()).`when`(tasksFacade).getAllOpen(notEq(PageRequest.of(0, 100)))
        doReturn(PageImpl((tasks + tasksOverdue).shuffled())).`when`(tasksFacade).getAllOpen(eq(PageRequest.of(0, 100)))

        // when - Overdue project gets recalculated
        dynamicProjectHandlers.recalculateProject(RecalculateProject(overdueProject.id))

        // then
        assertThat(dynamicProjectsFacade.getProjectById(overdueProject.id)!!.tasks).hasSameElementsAs(
            tasksOverdue.map { it.id }
        )
    }

    @Test
    fun `should recalculate Overdue dynamic project membership when tasks no longer meet conditions`() {
        // given - any project to generate default ones
        dynamicProjectHandlers.handleProjectCreated(ProjectCreated(ProjectId()))
        // and - Overdue project created as a result
        val overdueProject = dynamicProjectsFacade.getProjectByName("Overdue")
        assertThat(overdueProject).isNotNull
        overdueProject!!

        // and - list of tasks
        val tasks = listOf(
            TaskDto(name = "task_due_tomorrow", dueDate = LocalDate.now().plusDays(1)),
            TaskDto(name = "task_due_in_2_days", dueDate = LocalDate.now().plusDays(2)),
            TaskDto(name = "task_due_in_a_month", dueDate = LocalDate.now().plusDays(30))
        )

        var tasksOverdue = listOf(
            TaskDto(name = "task_due_yesterday", dueDate = LocalDate.now().minusDays(1)),
            TaskDto(name = "task_due_2_days_ago", dueDate = LocalDate.now().minusDays(2)),
            TaskDto(name = "task_due_month_ago", dueDate = LocalDate.now().minusDays(30))
        )
        doReturn(Page.empty<TaskDto>()).`when`(tasksFacade).getAllOpen(notEq(PageRequest.of(0, 100)))
        doReturn(PageImpl((tasks + tasksOverdue).shuffled())).`when`(tasksFacade).getAllOpen(eq(PageRequest.of(0, 100)))


        dynamicProjectHandlers.recalculateProject(RecalculateProject(overdueProject.id))

        // and tasks are added to the dynamic project
        assertThat(dynamicProjectsFacade.getProjectById(overdueProject.id)!!.tasks).hasSameElementsAs(
            tasksOverdue.map { it.id }
        )

        // when - tasks have changed
        tasksOverdue = tasksOverdue.map {
            TaskDto(
                id = it.id,
                name = it.name,
                completionDate = it.completionDate,
                dueDate = LocalDate.now().plus(1, ChronoUnit.MONTHS)
            )
        }
        doReturn(PageImpl((tasks + tasksOverdue).shuffled())).`when`(tasksFacade).getAllOpen(eq(PageRequest.of(0, 100)))

        // and - project gets recalculated again
        dynamicProjectHandlers.recalculateProject(RecalculateProject(overdueProject.id))

        // then
        assertThat(dynamicProjectsFacade.getProjectById(overdueProject.id)!!.tasks).isEmpty()
    }
}