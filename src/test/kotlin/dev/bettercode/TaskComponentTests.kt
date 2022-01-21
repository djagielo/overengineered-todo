package dev.bettercode

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class TaskComponentTests {

    @AfterEach
    fun cleanup() {
        getProjects().forEach {
            getTasksForProject(it).forEach {
                doDeleteTask(it)
            }
        }
    }

    @Test
    fun `should be able to create task`() {
        // given -
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
        getTask(id)
            // then
            .body("name", equalTo("todo"))
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

    private fun client() = given().baseUri("http://localhost:9999").contentType("application/json")
}