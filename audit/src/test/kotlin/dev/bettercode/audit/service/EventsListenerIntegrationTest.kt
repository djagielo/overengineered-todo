package dev.bettercode.audit.service

import dev.bettercode.audit.AuditTestConfiguration
import dev.bettercode.audit.infra.adapter.event.AuditSpringEventsListener
import dev.bettercode.commons.events.AuditLogCommand
import dev.bettercode.tasks.ProjectId
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
    fun `AuditLogCommand should add a message to Audit Log`() {
        // given
        val projectId = ProjectId()
        eventListener.handleAuditLogCmd(AuditLogCommand(message = "Project with id=ProjectId(uuid=${projectId.uuid}) has been reopened"))

        // when
        val audits = auditService.getAll(PageRequest.of(0, 2))

        // then
        Assertions.assertThat(audits.content.map { it.msg })
            .containsExactlyInAnyOrder("Project with id=ProjectId(uuid=${projectId.uuid}) has been reopened")
    }
}