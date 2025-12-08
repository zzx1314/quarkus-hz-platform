package org.huazhi.system.sysuser.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.huazhi.system.sysuser.entity.dto.SysUserDto;
import org.huazhi.util.JsonUtil;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
    public void testCreateUser() {
        String token = getAuthToken();
        SysUserDto newUser = new SysUserDto();
        newUser.setUsername("testuser");
        newUser.setPassword("Test123!");
        newUser.setRealName("TestUser");
        newUser.setOrgId(1L);
        newUser.setRole(1L);
        newUser.setEnable(1);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(JsonUtil.toJson(newUser))
                .when().post("/sysUser/create")
                .then()
                .statusCode(200)
                .body("code", equalTo(10200));
    }

    @Test
    @Order(2)
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
    @Order(3)
    public void testUpdateUserRole() {
        String token = getAuthToken();

        SysUserDto updateUserDto = new SysUserDto();
        updateUserDto.setId(1L);
        updateUserDto.setUsername("roleupdateuser");
        updateUserDto.setPassword("Role123!");
        updateUserDto.setRealName("RoleUpdateUser");
        updateUserDto.setOrgId(1L);
        updateUserDto.setRole(2L);

        // 更新用户角色
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(JsonUtil.toJson(updateUserDto))
                .when().put("/sysUser/update")
                .then()
                .statusCode(200)
                .body("code", equalTo(10200));
    }

    @Test
    @Order(4)
    public void testDeleteUser() {
        String token = getAuthToken();
        Long userId = 1L;
        // 删除用户
        given()
                .header("Authorization", "Bearer " + token)
                .when().delete("/sysUser/" + userId)
                .then()
                .statusCode(200);
    }
}
