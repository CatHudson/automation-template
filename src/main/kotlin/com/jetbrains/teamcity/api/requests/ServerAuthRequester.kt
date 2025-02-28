package com.jetbrains.teamcity.api.requests

import com.jetbrains.teamcity.api.models.BaseModel
import com.jetbrains.teamcity.api.models.ServerAuthSettings
import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.apache.http.HttpStatus

class ServerAuthRequester(private val specification: RequestSpecification) : BaseModel() {

    companion object {
        private const val SERVER_AUTH_SETTINGS_URL = "/app/rest/server/authSettings"
    }

    fun read(): ServerAuthSettings {
        return RestAssured
            .given()
            .spec(specification)
            .get(SERVER_AUTH_SETTINGS_URL)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract().`as`(ServerAuthSettings::class.java)
    }

    fun update(settings: ServerAuthSettings): ServerAuthSettings {
        return RestAssured
            .given()
            .spec(specification)
            .body(settings)
            .put(SERVER_AUTH_SETTINGS_URL)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract().`as`(ServerAuthSettings::class.java)
    }
}
