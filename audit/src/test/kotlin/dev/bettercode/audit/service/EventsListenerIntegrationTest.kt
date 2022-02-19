package dev.bettercode.audit.service

import dev.bettercode.audit.AuditTestConfiguration
import dev.bettercode.audit.infra.adapter.event.AuditSpringEventsListener
import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.TaskId
import dev.bettercode.tasks.application.projects.ProjectCompleted
import dev.bettercode.tasks.application.projects.ProjectCreated
import dev.bettercode.tasks.application.projects.ProjectReopened
import dev.bettercode.tasks.application.tasks.TaskCompleted
import dev.bettercode.tasks.application.tasks.TaskCreated
import dev.bettercode.tasks.application.tasks.TaskReopened
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [AuditTestConfiguration::class])
class EventsListenerIntegrationTest {

    @Autowired
    private lateinit var eventListener: AuditSpringEventsListener

    @Autowired
    private lateinit var auditService: AuditService

    @AfterEach
    fun afterEach() {
        auditService.deleteAll()
    }

    @Test
    fun `taskCreated should get logged in Audit log`() {
        // given
        val taskId = TaskId()
        eventListener.handleTaskCreated(TaskCreated(taskId))

        // when
        val audits = auditService.getAll(PageRequest.of(0, 2))

        // then
        Assertions.assertThat(audits.content.map { it.msg })
            .containsExactlyInAnyOrder("Task with id=TaskId(uuid=${taskId.uuid}) has been created")
    }

    @Test
    fun `taskCompleted should get logged in Audit log`() {
        // given
        val taskId = TaskId()
        eventListener.handleTaskCompleted(TaskCompleted(taskId))

        // when
        val audits = auditService.getAll(PageRequest.of(0, 2))

        // then
        Assertions.assertThat(audits.content.map { it.msg })
            .containsExactlyInAnyOrder("Task with id=TaskId(uuid=${taskId.uuid}) has been completed")
    }

    @Test
    fun `taskReopened should get logged in Audit log`() {
        // given
        val taskId = TaskId()
        eventListener.handleTaskReopened(TaskReopened(taskId))

        // when
        val audits = auditService.getAll(PageRequest.of(0, 2))

        // then
        Assertions.assertThat(audits.content.map { it.msg })
            .containsExactlyInAnyOrder("Task with id=TaskId(uuid=${taskId.uuid}) has been reopened")
    }

    @Test
    fun `projectCreated should get logged in Audit log`() {
        // given
        val projectId = ProjectId()
        eventListener.handleProjectCompleted(ProjectCreated(projectId))

        // when
        val audits = auditService.getAll(PageRequest.of(0, 2))

        // then
        Assertions.assertThat(audits.content.map { it.msg })
            .containsExactlyInAnyOrder("Project with id=ProjectId(uuid=${projectId.uuid}) has been created")
    }

    @Test
    fun `projectCompleted should get logged in Audit log`() {
        // given
        val projectId = ProjectId()
        eventListener.handleProjectCompleted(ProjectCompleted(projectId))

        // when
        val audits = auditService.getAll(PageRequest.of(0, 2))

        // then
        Assertions.assertThat(audits.content.map { it.msg })
            .containsExactlyInAnyOrder("Project with id=ProjectId(uuid=${projectId.uuid}) has been completed")
    }

    @Test
    fun `projectReopened should get logged in Audit log`() {
        // given
        val projectId = ProjectId()
        eventListener.handleProjectReopened(ProjectReopened(projectId))

        // when
        val audits = auditService.getAll(PageRequest.of(0, 2))

        // then
        Assertions.assertThat(audits.content.map { it.msg })
            .containsExactlyInAnyOrder("Project with id=ProjectId(uuid=${projectId.uuid}) has been reopened")
    }
}