package dev.bettercode

import com.nimbusds.jose.util.Base64
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.startsWith
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Testcontainers
class TasksServiceComponentTests {

    @AfterEach
    fun cleanup() {
        getProjects().forEach {
            getTasksForProject(it).forEach {
                doDeleteTask(it)
            }
        }
    }

    class KGenericContainer(image: DockerImageName) : GenericContainer<KGenericContainer>(image)
    class KMariaDBContainer(image: String) : MariaDBContainer<KMariaDBContainer>(image)

    companion object {
        private val oktaIssuer: String by lazy {
            System.getenv("CT_OKTA_ISSUER")
        }

        private val clientId: String by lazy {
            System.getenv("CT_CLIENT_ID")
        }

        private val clientSecret: String by lazy {
            System.getenv("CT_CLIENT_SECRET")
        }

        private val testUser: String by lazy {
            System.getenv("CT_TEST_USER")
        }

        private val testPassword: String by lazy {
            System.getenv("CT_TEST_PASSWORD")
        }

        private val circleBranch: String by lazy {
            System.getenv("CIRCLE_BRANCH")
        }

        var network: Network = Network.newNetwork()

        val service: GenericContainer<*> =
            KGenericContainer(DockerImageName.parse("bettercode.dev/overengineered-todo:${circleBranch}"))
                .withNetwork(network)
                .withExposedPorts(9999).withEnv(
                    mapOf(
                        "OKTA_CLIENT_ID" to clientId,
                        "OKTA_CLIENT_SECRET" to clientSecret,
                        "OKTA_ISSUER" to oktaIssuer,
                        "SPRING_PROFILES_ACTIVE" to "ct"
                    )
                )
                .waitingFor(Wait.forHttp("/actuator/health"))


        val db: MariaDBContainer<*> = KMariaDBContainer("mariadb:10.6")
            .withNetwork(network)
            .withNetworkAliases("mariadb")

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            db.start()
            service
                .withEnv("MARIA_DB_URL", "jdbc:mariadb://mariadb:3306/test")
                .withEnv("MARIA_DB_USER", db.username)
                .withEnv("MARIA_DB_PASS", db.password)
                .start()
        }

        @AfterAll
        internal fun tearDown() {
            service.stop()
            db.stop()
        }
    }

    @Test
    fun `should be able to create task`() {
        // given
        val id = postTask("todo")

        // when
        getTask(id)
            // then
            .body("name", equalTo("todo"))
    }

    @Test
    fun `should be able to complete task`() {
        // given
        val id = postTask("todo")

        // when
        completeTask(id = id)


        getTask(id)
            // then
            .body("name", equalTo("todo"))
            .body("completionDate", startsWith(LocalDate.now().format(DateTimeFormatter.ISO_DATE)))
    }

    @Test
    fun `should be able to reopen task the same day`() {
        // given
        val id = postTask("todo")
        completeTask(id = id)

        // when
        reopenTask(id = id)

        getTask(id)
            // then
            .body("name", equalTo("todo"))
            .body("completionDate", equalTo(null))
    }

    private fun completeTask(id: String) {
      updateStatus(id, completed = true)
    }

    private fun reopenTask(id: String) {
        updateStatus(id, completed = false)
    }

    private fun updateStatus(id: String, completed: Boolean) {
        client().body(
            """
                {
                    "completed": $completed
                }
            """.trimIndent()
        ).`when`().
        put("/tasks/$id/status").then().statusCode(200)
    }

    private fun getTask(id: String) = client().get("/tasks/${id}")
        // then
        .then()
        .statusCode(200)
        .contentType("application/json")

    private fun postTask(name: String): String {
        return client().body(
            """
                    {
                        "name": "$name"
                    }
                """
        ).`when`().post("/tasks").then().statusCode(201).extract().body().jsonPath().get("id.uuid")
    }

    private fun doDeleteTask(uuid: String) {
        client().delete("/tasks/$uuid")
            .then()
            .statusCode(204)
    }

    private fun getProjects(): List<String> = client().get("/projects/")
        .then()
        .statusCode(200)
        .contentType("application/json")
        .extract().body().jsonPath().get("content.id.uuid")

    private fun getTasksForProject(projectId: String): List<String> {
        return client().get("/projects/${projectId}/tasks").then()
            .statusCode(200)
            .contentType("application/json")
            .extract().body().jsonPath().get("content.id.uuid")
    }

    private fun client() =
        given().auth().oauth2(accessToken).baseUri("http://localhost:${service.getMappedPort(9999)}")
            .contentType("application/json")

    private val accessToken: String by lazy {
        given().headers(
            mapOf(
                "accept" to "application/json",
                "authorization" to "Basic ${Base64.encode("$clientId:$clientSecret")}",
            )
        )
            .contentType("application/x-www-form-urlencoded")
            .formParams(
                mapOf(
                    "grant_type" to "password",
                    "username" to testUser,
                    "password" to testPassword,
                    "scope" to "openid"
                )
            )
            .post("$oktaIssuer/v1/token")
            .then()
            .extract().body().jsonPath().get("access_token")
    }
}