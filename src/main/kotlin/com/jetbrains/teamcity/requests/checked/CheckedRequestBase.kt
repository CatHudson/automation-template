package com.jetbrains.teamcity.requests.checked

import com.jetbrains.teamcity.enums.Endpoint
import com.jetbrains.teamcity.models.BaseModel
import com.jetbrains.teamcity.requests.CRUDInterface
import com.jetbrains.teamcity.requests.Request
import com.jetbrains.teamcity.requests.unchecked.UncheckedRequestBase
import io.restassured.specification.RequestSpecification
import org.apache.http.HttpStatus

@Suppress("unchecked_cast")
class CheckedRequestBase<T : BaseModel>(
    private val spec: RequestSpecification,
    private val endpoint: Endpoint,
) : Request(spec, endpoint), CRUDInterface {

    private val uncheckedRequestBase = UncheckedRequestBase(spec, endpoint)

    override fun create(model: BaseModel): T {
        return uncheckedRequestBase
            .create(model)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract().`as`(endpoint.modelClass) as T
    }

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
            .statusCode(HttpStatus.SC_OK)
    }
}
