package dev.bettercode.audit.service

import dev.bettercode.audit.AuditTestConfiguration
import dev.bettercode.audit.repository.AuditLog
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [AuditTestConfiguration::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnableAutoConfiguration(exclude = [ SecurityAutoConfiguration::class])
@AutoConfigureMockMvc(addFilters = false)
internal class AuditServiceIntegrationTest {

    @Autowired
    lateinit var auditService: AuditService

    @AfterEach
    fun afterEach() {
        auditService.deleteAll()
    }

    @Test
    fun `should be able save and retrieved paged AuditLog entries`() {
        // given
        listOf(
            anAuditLog(msg = "Test audit log1"),
            anAuditLog(msg = "Test audit log2"),
            anAuditLog(msg = "Test audit log3"),
        ).forEach {
            auditService.save(it)
        }
        // when
        val firstPage = auditService.getAll(PageRequest.of(0, 2))
        val secondPage = auditService.getAll(PageRequest.of(1, 2))

        // then
        Assertions.assertThat(firstPage.content.map { it.msg })
            .containsExactlyInAnyOrder("Test audit log1", "Test audit log2")
        Assertions.assertThat(secondPage.content.map { it.msg }).containsExactlyInAnyOrder("Test audit log3")
    }

    private fun anAuditLog(msg: String): AuditLog {
        return AuditLog(msg = msg)
    }

}