package org.huazhi.oauth.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class AuthControllerTest {

    @Test
    public void testLoginWithValidCredentials() {
        given()
                .contentType(ContentType.URLENC)
                .formParam("grant_type", "password")
                .formParam("username", "sysadmin")
                .formParam("password", "l4dcYnPtG6AhxUBHVJwgUQ==")
                .formParam("scope", "server")
                .when().post("/token")
                .then()
                .statusCode(200)
                .body("access_token", notNullValue());
    }

    @Test
    public void testLoginWithInvalidPassword() {
        given()
                .contentType(ContentType.URLENC)
                .formParam("grant_type", "password")
                .formParam("username", "sysadmin")
                .formParam("password", "l5dcYnPtG6AhxUBHVJwgUQ==")
                .formParam("scope", "server")
                .when().post("/token")
                .then()
                .statusCode(401)
                .body("code", equalTo(10400))
                .body("msg", equalTo("密码错误"));
    }

    @Test
    public void testLoginWithNonExistentUser() {
        given()
                .contentType(ContentType.URLENC)
                .formParam("grant_type", "password")
                .formParam("username", "nonexistent")
                .formParam("password", "l5dcYnPtG6AhxUBHVJwgUQ==")
                .formParam("scope", "server")
                .when().post("/token")
                .then()
                .statusCode(401)
                .body("code", equalTo(10400))
                .body("msg", equalTo("用户名不存在"));
    }

    @Test
    public void testTokenRefresh() {

        // 1. 登录获取 access_token & refresh_token
        ValidatableResponse response = given()
                .contentType(ContentType.URLENC)
                .formParam("grant_type", "password")
                .formParam("username", "sysadmin")
                .formParam("password", "l4dcYnPtG6AhxUBHVJwgUQ==")
                .formParam("scope", "server")
                .when().post("/token")
                .then()
                .statusCode(200);

        String accessToken = response.extract().path("access_token");
        String refreshToken = response.extract().path("refresh_token");

        // 2. 调用刷新接口，带 Authorization: Bearer <access_token>
        given()
                .header("Authorization", "Bearer " + accessToken)
                .when().post("/token/refresh/" + refreshToken)
                .then()
                .statusCode(200)
                .body("access_token", notNullValue());
    }

}
