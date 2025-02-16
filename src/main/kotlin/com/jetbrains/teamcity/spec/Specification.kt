package com.jetbrains.teamcity.spec

import com.jetbrains.teamcity.configuration.Configuration
import com.jetbrains.teamcity.models.User
import io.restassured.authentication.BasicAuthScheme
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification

object Specification {

    private fun reqBuilder(): RequestSpecBuilder {
        return RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .setBaseUri("http://${Configuration.getProperty("host")}:${Configuration.getProperty("port")}")
            .addFilters(listOf(RequestLoggingFilter(), ResponseLoggingFilter()))
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
        basicAuthScheme.userName = "" //empty username is required for a superuser auth
        basicAuthScheme.password = Configuration.getProperty("super-user-token").toString()
        return reqBuilder()
            .setAuth(basicAuthScheme)
            .build()
    }
}
