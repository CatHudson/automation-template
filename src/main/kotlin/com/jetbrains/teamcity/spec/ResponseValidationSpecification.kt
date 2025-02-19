package com.jetbrains.teamcity.spec

import io.restassured.builder.ResponseSpecBuilder
import io.restassured.specification.ResponseSpecification
import org.apache.http.HttpStatus
import org.hamcrest.Matchers

object ResponseValidationSpecifications {

    private val responseSpecBuilder = ResponseSpecBuilder()

    fun checkProjectWithNameAlreadyExist(projectName: String): ResponseSpecification {
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_BAD_REQUEST)
        responseSpecBuilder.expectBody(
            Matchers.containsString("Project with this name already exists: $projectName")
        )
        return responseSpecBuilder.build()
    }

    fun checkProjectWithIdAlreadyExist(projectId: String): ResponseSpecification {
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_BAD_REQUEST)
        responseSpecBuilder.expectBody(
            Matchers.containsString("Project ID \"$projectId\" is already used by another project")
        )
        return responseSpecBuilder.build()
    }

    fun checkForbiddenError(): ResponseSpecification {
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_FORBIDDEN)
        responseSpecBuilder.expectBody(
            Matchers.containsString("Access denied. Check the user has enough permissions to perform the operation.")
        )
        return responseSpecBuilder.build()
    }

    fun checkUnauthorizedError(): ResponseSpecification {
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_UNAUTHORIZED)
        responseSpecBuilder.expectBody(
            Matchers.containsString("Authentication required")
        )
        return responseSpecBuilder.build()
    }
}
