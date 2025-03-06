package com.jetbrains.teamcity.api

import com.jetbrains.teamcity.api.spec.Specification
import io.restassured.RestAssured.given
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test

class DummyTest : BaseApiTest() {

    @Test
    fun `an unauthorized user should be able to get all projects`() {
        given()
            .spec(Specification.unAuthSpec())
            .get("/app/rest/projects")
            .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED)
    }

    @Test
    fun `an authorized user should be able to get all projects`() {
        given()
            .spec(Specification.authSpec())
            .get("/app/rest/projects")
            .then()
            .statusCode(HttpStatus.SC_OK)
    }
}
