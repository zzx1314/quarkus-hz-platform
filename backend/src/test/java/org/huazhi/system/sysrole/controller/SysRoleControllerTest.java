package org.huazhi.system.sysrole.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.huazhi.system.sysrole.entity.SysRole;
import org.huazhi.util.JsonUtil;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SysRoleControllerTest {

    private static String authToken;

    private static Long createdRoleId;

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

    private Long getRoleId() {
        return createdRoleId;
    }

    @Test
    @Order(1)
    public void testCreateRole() {
        String token = getAuthToken();
        SysRole newRole = new SysRole();
        newRole.setName("测试角色");
        newRole.setCode("TEST_ROLE");
        newRole.setDescription("测试用角色");

        createdRoleId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(JsonUtil.toJson(newRole))
                .when().post("/sysRole/create")
                .then()
                .statusCode(200)
                .extract().path("data");
    }

    @Test
    @Order(2)
    public void testGetRoleList() {
        String token = getAuthToken();
        given()
                .header("Authorization", "Bearer " + token)
                .when().get("/sysRole/getPage")
                .then()
                .statusCode(200)
                .body("data.records.size()", greaterThan(0));
    }

    @Test
    @Order(3)
    public void testUpdateRole() {
        String token = getAuthToken();

        SysRole updateRole = new SysRole();
        updateRole.setId(getRoleId());
        updateRole.setName("更新测试角色");
        updateRole.setCode("UPDATE_TEST_ROLE");
        updateRole.setDescription("更新测试用角色");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(JsonUtil.toJson(updateRole))
                .when().put("/sysRole/update")
                .then()
                .statusCode(200)
                .body("code", equalTo(10200));
    }

    @Test
    @Order(4)
    public void testDeleteRole() {
        String token = getAuthToken();
        given()
                .header("Authorization", "Bearer " + token)
                .when().delete("/sysRole/" + getRoleId())
                .then()
                .statusCode(200);
    }
}