package org.huazhi.drones.device.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.huazhi.drones.business.device.entity.dto.DronesDeviceDto;
import org.huazhi.util.JsonUtil;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeviceControllerTest {

    private static String authToken;

    private static Integer createdDeviceId;

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

    private Integer getDeviceId() {
        return createdDeviceId;
    }

    @Test
    @Order(1)
    public void testCreateDevice() {
        System.out.println("开始创建设备...");
        String token = getAuthToken();
        DronesDeviceDto newDevice = new DronesDeviceDto();
        newDevice.setDeviceId("DEV-001");
        newDevice.setDeviceName("TestDevice");
        newDevice.setDeviceType("Drone");
        newDevice.setModel("X200");
        newDevice.setStatus("ACTIVE");
        newDevice.setDeviceIp("192.168.1.100");
        newDevice.setSpeed("10");
        newDevice.setHeight("5");
        newDevice.setBattery("80");
        // 创建设备
        createdDeviceId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(JsonUtil.toJson(newDevice))
                .when().post("/dronesDevice/create")
                .then()
                .statusCode(200)
                .extract().path("data");
        System.out.println("创建设备成功，设备ID为：" + createdDeviceId);
    }

    @Test
    @Order(2)
    public void testGetDeviceList() {
        System.out.println("获取设备列表，当前创建设备ID为：" + getDeviceId());
        String token = getAuthToken();
        given()
                .header("Authorization", "Bearer " + token)
                .when().get("/dronesDevice/getPage")
                .then()
                .statusCode(200)
                .body("data.records.size()", greaterThan(0));
    }

    @Test
    @Order(3)
    public void testUpdateDevice() {
        System.out.println("获取设备列表，当前创建设备ID为：" + getDeviceId());
        String token = getAuthToken();

        DronesDeviceDto updateDeviceDto = new DronesDeviceDto();
        updateDeviceDto.setId(Long.valueOf(getDeviceId()));
        updateDeviceDto.setDeviceId("DEV-001");
        updateDeviceDto.setDeviceName("UpdatedDevice");
        updateDeviceDto.setDeviceType("Drone");
        updateDeviceDto.setModel("X300");
        updateDeviceDto.setStatus("MAINTENANCE");

        // 更新设备
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(JsonUtil.toJson(updateDeviceDto))
                .when().put("/dronesDevice/update")
                .then()
                .statusCode(200)
                .body("code", equalTo(10200));
    }

    @Test
    @Order(4)
    public void testDeleteDevice() {
         System.out.println("获取设备列表，当前创建设备ID为：" + getDeviceId());
        String token = getAuthToken();
        // 删除设备
        given()
                .header("Authorization", "Bearer " + token)
                .when().delete("/dronesDevice/" + getDeviceId())
                .then()
                .statusCode(200);
    }
}
