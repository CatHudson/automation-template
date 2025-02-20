package com.jetbrains.teamcity.requests.checked

import com.jetbrains.teamcity.enums.Endpoint
import com.jetbrains.teamcity.generators.TestDataStorage
import com.jetbrains.teamcity.models.BaseModel
import com.jetbrains.teamcity.requests.CRUDInterface
import com.jetbrains.teamcity.requests.Request
import com.jetbrains.teamcity.requests.SearchInterface
import com.jetbrains.teamcity.requests.unchecked.UncheckedRequestBase
import io.restassured.specification.RequestSpecification
import org.apache.http.HttpStatus

@Suppress("unchecked_cast")
class CheckedRequestBase<T : BaseModel>(
    private val spec: RequestSpecification,
    private val endpoint: Endpoint,
) : Request(spec, endpoint), CRUDInterface, SearchInterface {

    private val uncheckedRequestBase = UncheckedRequestBase(spec, endpoint)

    override fun create(model: BaseModel): T {
        val createdModel = uncheckedRequestBase
            .create(model)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract().`as`(endpoint.modelClass) as T

        TestDataStorage.addCreatedEntity(endpoint, createdModel)
        return createdModel
    }

    /**
     * You can pass a parameter name to search by something other than id
     */
    override fun read(id: String): T {
        return uncheckedRequestBase
            .read(id)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract().`as`(endpoint.modelClass) as T
    }

    override fun update(id: String, model: BaseModel): T {
        return uncheckedRequestBase
            .update(id, model)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract().`as`(endpoint.modelClass) as T
    }

    override fun delete(id: String): Any {
        return uncheckedRequestBase
            .delete(id)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_NO_CONTENT)
    }

    override fun search(query: String): List<T> {
        return uncheckedRequestBase
            .search(query)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract().jsonPath().getList<T>("project")
    }

    override fun filter(filters: Map<String, String>): List<T> {
        return uncheckedRequestBase
            .filter(filters)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract().jsonPath().getList<T>("project")
    }
}
