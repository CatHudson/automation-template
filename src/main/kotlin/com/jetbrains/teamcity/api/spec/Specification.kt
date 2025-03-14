package com.jetbrains.teamcity.api.spec

import com.github.viclovsky.swagger.coverage.FileSystemOutputWriter
import com.github.viclovsky.swagger.coverage.SwaggerCoverageRestAssured
import com.jetbrains.teamcity.api.configuration.Configuration
import com.jetbrains.teamcity.api.models.User
import io.qameta.allure.restassured.AllureRestAssured
import io.restassured.authentication.BasicAuthScheme
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import java.nio.file.Paths

object Specification {

    private fun reqBuilder(): RequestSpecBuilder {
        return RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .setBaseUri("http://${Configuration.getProperty("host")}:${Configuration.getProperty("port")}")
            .addFilter(AllureRestAssured())
            .addFilter(RequestLoggingFilter())
            .addFilter(ResponseLoggingFilter())
            .addFilter(
                SwaggerCoverageRestAssured(
                    FileSystemOutputWriter(
                        Paths.get("build/" + com.github.viclovsky.swagger.coverage.SwaggerCoverageConstants.OUTPUT_DIRECTORY)
                    )
                )
            )
    }

    fun unAuthSpec(): RequestSpecification {
        return reqBuilder().build()
    }

    fun authSpec(): RequestSpecification {
        val basicAuthScheme = BasicAuthScheme()
        basicAuthScheme.userName = Configuration.getProperty("username").toString()
        basicAuthScheme.password = Configuration.getProperty("password").toString()
        return reqBuilder()
            .setAuth(basicAuthScheme)
            .build()
    }

    fun authSpec(user: User): RequestSpecification {
        val basicAuthScheme = BasicAuthScheme()
        basicAuthScheme.userName = user.username
        basicAuthScheme.password = user.password
        return reqBuilder()
            .setAuth(basicAuthScheme)
            .build()
    }

    fun superUserSpec(): RequestSpecification {
        val basicAuthScheme = BasicAuthScheme()
        basicAuthScheme.userName = "" // empty username is required for a superuser auth
        basicAuthScheme.password = Configuration.getProperty("super-user-token").toString()
        return reqBuilder()
            .setAuth(basicAuthScheme)
            .build()
    }

    fun mockSpec(): RequestSpecification {
        return reqBuilder()
            .setBaseUri("http://localhost:8081")
            .build()
    }
}
