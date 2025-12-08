package org.huazhi.drones.device.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class DeviceControllerTest {

    @Test
    public void testDeviceRegistration() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"deviceId\":\"DEV-001\",\"model\":\"X200\"}")
            .when().post("/api/device/register")
            .then()
                .statusCode(201)
                .body("deviceId", equalTo("DEV-001"))
                .body("status", equalTo("ACTIVE"));
    }

    @Test
    public void testGetDeviceInfo() {
        // 先注册设备
        given()
            .contentType(ContentType.JSON)
            .body("{\"deviceId\":\"DEV-002\",\"model\":\"X300\"}")
            .post("/api/device/register");

        given()
            .when().get("/api/device/DEV-002")
            .then()
                .statusCode(200)
                .body("deviceId", equalTo("DEV-002"))
                .body("model", equalTo("X300"));
    }

    @Test
    public void testUpdateDeviceStatus() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"deviceId\":\"DEV-003\",\"status\":\"MAINTENANCE\"}")
            .when().put("/api/device/status")
            .then()
                .statusCode(200)
                .body("status", equalTo("MAINTENANCE"));
    }

    @Test
    public void testGetNonExistentDevice() {
        given()
            .when().get("/api/device/INVALID-123")
            .then()
                .statusCode(404)
                .body("message", equalTo("Device not found"));
    }
}
