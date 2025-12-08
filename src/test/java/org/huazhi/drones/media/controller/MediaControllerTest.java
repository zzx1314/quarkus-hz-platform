package org.huazhi.drones.media.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.huazhi.drones.media.entity.dto.DronesMediaDto;
import org.huazhi.util.JsonUtil;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MediaControllerTest {

    private static String authToken;

    private static Integer createdMediaId;

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

    private Integer getMediaId() {
        return createdMediaId;
    }

    @Test
    @Order(1)
    public void testUploadMedia() {
        System.out.println("开始上传媒体文件...");
        String token = getAuthToken();
        DronesMediaDto mediaDto = new DronesMediaDto();
        mediaDto.setMediaName("test-image.jpg");
        mediaDto.setMediaType("image/jpeg");
        mediaDto.setRemarks("测试图片");
        String mediaInfo = JsonUtil.toJson(mediaDto);
        createdMediaId = given()
                .header("Authorization", "Bearer " + token)
                .multiPart("file", "test-image.jpg", "test content".getBytes(), "image/jpeg")
                .multiPart("mediaInfo", mediaInfo)
                .when().post("/dronesMedia/uploadFile")
                .then()
                .statusCode(200)
                .body("code", equalTo(10200))
                .extract().path("data");
        System.out.println("上传媒体文件成功，媒体ID为：" + createdMediaId);
    }

    @Test
    @Order(2)
    public void testGetMediaList() {
        System.out.println("获取媒体文件列表，当前媒体ID为：" + getMediaId());
        String token = getAuthToken();
        given()
                .header("Authorization", "Bearer " + token)
                .when().get("/dronesMedia/getPage")
                .then()
                .statusCode(200)
                .body("data.records.size()", greaterThan(0));
    }

    @Test
    @Order(3)
    public void testGetMediaById() {
        System.out.println("根据ID获取媒体文件信息，当前媒体ID为：" + getMediaId());
        String token = getAuthToken();
        given()
                .header("Authorization", "Bearer " + token)
                .when().get("/dronesMedia/getByDto?id=" + getMediaId())
                .then()
                .statusCode(200)
                .body("data.size()", greaterThan(0));
    }

    @Test
    @Order(4)
    public void testDeleteMedia() {
        System.out.println("删除媒体文件，当前媒体ID为：" + getMediaId());
        String token = getAuthToken();
        given()
                .header("Authorization", "Bearer " + token)
                .when().delete("/dronesMedia/" + getMediaId())
                .then()
                .statusCode(200)
                .body("code", equalTo(10200));
    }
}
