package AldiAPITests;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskApiTest extends BaseTest {

    private static int createdTaskId;

    @Test
    @Order(1)
    public void testCreateTask() {
        Map<String, Object> task = new HashMap<>();
        task.put("title", "Write API tests");
        task.put("description", "Create sample RestAssured tests");
        task.put("status", "pending");

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .body(task)
                        .when()
                        .post("/tasks")
                        .then()
                        .statusCode(201)
                        .body("id", notNullValue())
                        .body("title", equalTo("Write API tests"))
                        .extract().response();

        createdTaskId = response.path("id");
        Assertions.assertTrue(createdTaskId > 0, "Task ID should be greater than 0");
    }

    @Test
    @Order(2)
    public void testGetTaskById() {
        given()
                .pathParam("id", createdTaskId)
                .when()
                .get("/tasks/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(createdTaskId))
                .body("title", equalTo("Write API tests"));
    }

    @Test
    @Order(3)
    public void testUpdateTaskById() {
        Map<String, Object> updatedTask = new HashMap<>();
        updatedTask.put("title", "Write API and UI tests");
        updatedTask.put("status", "in-progress");

        given()
                .header("Content-Type", "application/json")
                .pathParam("id", createdTaskId)
                .body(updatedTask)
                .when()
                .put("/tasks/{id}")
                .then()
                .statusCode(200)
                .body("title", equalTo("Write API and UI tests"))
                .body("status", equalTo("in-progress"));
    }

    @Test
    @Order(4)
    public void testDeleteTaskById() {
        given()
                .pathParam("id", createdTaskId)
                .when()
                .delete("/tasks/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    public void testGetDeletedTaskReturnsNotFound() {
        given()
                .pathParam("id", createdTaskId)
                .when()
                .get("/tasks/{id}")
                .then()
                .statusCode(404);
    }
}
