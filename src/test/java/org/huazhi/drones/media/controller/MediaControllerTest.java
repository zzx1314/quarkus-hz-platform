package org.huazhi.drones.media.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class MediaControllerTest {

    @Test
    public void testUploadMedia() {
        given()
            .multiPart("file", "test-image.jpg", "test content".getBytes(), "image/jpeg")
            .when().post("/api/media/upload")
            .then()
                .statusCode(200)
                .body("mediaId", notNullValue())
                .body("fileType", equalTo("image/jpeg"));
    }

    @Test
    public void testGetMediaMetadata() {
        String mediaId = given()
            .multiPart("file", "test-file.txt", "text content".getBytes(), "text/plain")
            .post("/api/media/upload")
            .then()
                .extract().path("mediaId");

        given()
            .when().get("/api/media/" + mediaId + "/metadata")
            .then()
                .statusCode(200)
                .body("fileName", equalTo("test-file.txt"))
                .body("fileType", equalTo("text/plain"));
    }

    @Test
    public void testGetInvalidMedia() {
        given()
            .when().get("/api/media/invalid-id-123/metadata")
            .then()
                .statusCode(404)
                .body("message", equalTo("Media not found"));
    }

    @Test
    public void testDeleteMedia() {
        String mediaId = given()
            .multiPart("file", "temp-file", "content".getBytes(), "application/octet-stream")
            .post("/api/media/upload")
            .then()
                .extract().path("mediaId");

        given()
            .when().delete("/api/media/" + mediaId)
            .then()
                .statusCode(204);
    }
}
