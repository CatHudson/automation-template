package com.jetbrains.teamcity.api.requests

import com.jetbrains.teamcity.api.models.Agent
import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers

class AgentAuthRequester(private val specification: RequestSpecification) {

    companion object {
        private const val GET_ALL_AGENTS_URL = "/app/rest/agents?locator=authorized:any"
        private const val AUTHORIZE_AGENT_URL = "/app/rest/agents/id:%s/authorized"
    }

    fun getAllAgents(): List<Agent> {
        return RestAssured
            .given()
            .spec(specification)
            .get(GET_ALL_AGENTS_URL)
            .then()
            .assertThat()
            .statusCode(200)
            .extract().jsonPath().getList("agent", Agent::class.java)
    }

    fun authorizeAgent(agent: Agent) {
        RestAssured
            .given()
            .spec(specification)
            .body("true")
            .put(AUTHORIZE_AGENT_URL.format(agent.id))
            .then()
            .assertThat()
            .statusCode(200)
            .body(Matchers.containsString("true"))
    }
}
