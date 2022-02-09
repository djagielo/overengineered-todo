package dev.bettercode.audit.service

import dev.bettercode.audit.repository.AuditLog
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class AuditServiceIntegrationTest {

    @Autowired
    lateinit var auditService: AuditService

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
        assertThat(firstPage.content.map { it.msg }).containsExactlyInAnyOrder("Test audit log1", "Test audit log2")
        assertThat(secondPage.content.map { it.msg }).containsExactlyInAnyOrder("Test audit log3")
    }

    private fun anAuditLog(msg: String): AuditLog {
        return AuditLog(msg = msg)
    }

}