package org.huazhi.system.sysuser.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class SystemUserControllerTest {

    private static String authToken;

    @BeforeAll
    public static void setup() {
        authToken = given()
            .contentType(ContentType.URLENC)
            .formParam("grant_type", "password")
            .formParam("username", "sysadmin")
            .formParam("password", "l4dcYnPtG6AhxUBHVJwgUQ==")
            .formParam("scope", "server")
            .when().post("/api/token")
            .then()
                .statusCode(200)
                .extract().path("access_token");
    }

    private String getAuthToken() {
        return authToken;
    }

    @Test
    public void testCreateUser() {
        String token = getAuthToken();
        
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body("{\"username\":\"testuser\",\"password\":\"Test123!\"}")
            .when().post("/sysUser/create")
            .then()
                .statusCode(200)
                .body("data", equalTo(true));
    }

    @Test
    public void testGetUserList() {
        String token = getAuthToken();
        given()
            .header("Authorization", "Bearer " + token)
            .when().get("/sysUser/getPage")
            .then()
                .statusCode(200)
                .body("data.records.size()", greaterThan(0));
    }

    @Test
    public void testUpdateUserRole() {
        String token = getAuthToken();

        // 创建测试用户
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body("{\"username\":\"updateuser\",\"password\":\"Pass123!\"}")
            .post("/sysUser/create");

        // 更新用户角色
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body("{\"roles\":[1]}") // 假设ADMIN角色的ID是1
            .when().put("/sysUser/update")
            .then()
                .statusCode(200)
                .body("data.roles.size()", greaterThan(0));
    }

    @Test
    public void testDeleteUser() {
        String token = getAuthToken();

        // 创建测试用户
        Long userId = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body("{\"username\":\"deleteuser\",\"password\":\"Temp123!\"}")
            .post("/sysUser/create")
            .then()
                .extract().path("data.id");

        // 删除用户
        given()
            .header("Authorization", "Bearer " + token)
            .when().delete("/sysUser/" + userId)
            .then()
                .statusCode(200);
    }
}
