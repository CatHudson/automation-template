package com.jetbrains.teamcity.api

import com.jetbrains.teamcity.BaseTest
import com.jetbrains.teamcity.api.generators.TestDataGenerator
import com.jetbrains.teamcity.api.models.AuthModules
import com.jetbrains.teamcity.api.models.ServerAuthSettings
import com.jetbrains.teamcity.api.requests.AgentAuthRequester
import com.jetbrains.teamcity.api.requests.ServerAuthRequester
import com.jetbrains.teamcity.api.spec.Specification
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import kotlin.properties.Delegates

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BaseApiTest : BaseTest() {
    private val serverAuthRequester = ServerAuthRequester(Specification.superUserSpec())
    private val agentAuthRequester = AgentAuthRequester(Specification.superUserSpec())
    private lateinit var initialAuthModules: AuthModules
    private var initialPerProjectPermissions by Delegates.notNull<Boolean>()

    @BeforeAll
    fun setUpServerPermissions() {
        val initSettings = serverAuthRequester.read()
        println(initSettings)
        initialPerProjectPermissions = initSettings.perProjectPermissions
        initialAuthModules = initSettings.modules

        val newAuthModules = TestDataGenerator.generate(AuthModules::class.java)

        serverAuthRequester.update(
            ServerAuthSettings(
                perProjectPermissions = true,
                modules = newAuthModules
            )
        )
    }

    @BeforeAll
    fun authorizeDefaultAgent() {
        val agent = agentAuthRequester.getAllAgents().first()
        agentAuthRequester.authorizeAgent(agent)
    }

    @AfterAll
    fun cleanUpServerPermissions() {
        serverAuthRequester.update(
            ServerAuthSettings(
                perProjectPermissions = initialPerProjectPermissions,
                modules = initialAuthModules
            )
        )
    }
}
