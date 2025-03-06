package com.jetbrains.teamcity.api

import com.jetbrains.teamcity.api.requests.AgentAuthRequester
import com.jetbrains.teamcity.api.spec.Specification
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("Agent-setup")
class SetUpAgentTest : BaseApiTest() {

    private val agentAuthRequester = AgentAuthRequester(Specification.superUserSpec())

    @Test
    fun authorizeDefaultAgent() {
        val agent = agentAuthRequester.getAllAgents().first()
        agentAuthRequester.authorizeAgent(agent)
    }
}
