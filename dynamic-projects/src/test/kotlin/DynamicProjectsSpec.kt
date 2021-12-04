import dev.bettercode.dynamicprojects.DynamicProjectsConfiguration
import dev.bettercode.dynamicprojects.DynamicProjectsFacade
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

class DynamicProjectsSpec {

    private inline fun <T> notEq(value: T): T {
        return AdditionalMatchers.not(ArgumentMatchers.eq(value)) ?: value
    }

    private val dynamicProjectsFacade: DynamicProjectsFacade =
        DynamicProjectsConfiguration.dynamicProjectsFacade()

    private val tasksFacade: TasksFacade = mock(TasksFacade::class.java)

    private val dynamicProjectHandlers: DynamicProjectHandlers =
        DynamicProjectsConfiguration.dynamicProjectHandlers(tasksFacade, )

    @Test
    fun `should create predefined dynamic-projects after first project gets created`() {
        // given - any project gets created
        dynamicProjectHandlers.handleProjectCreated(ProjectCreated(ProjectId()))

        // when
        val dynamicProjects = dynamicProjectsFacade.getProjects()

        // then
        assertThat(dynamicProjects.map { it.name }).containsExactlyInAnyOrder(
            "Completed today", "Today", "Overdue", "Completed last week"
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
            TaskDto(name = "task_due_yesterday", dueDate = LocalDate.now().plusDays(1)),
            TaskDto(name = "task_due_2_days_ago", dueDate = LocalDate.now().plusDays(2)),
            TaskDto(name = "task_due_month_ago", dueDate = LocalDate.now().plusDays(30))
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
}