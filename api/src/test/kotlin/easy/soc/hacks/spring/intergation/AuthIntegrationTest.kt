package easy.soc.hacks.spring.intergation

import easy.soc.hacks.spring.domain.User
import io.qameta.allure.Description
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpStatus.OK
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Testcontainers

@Epic("Integration test")
@Feature("Authorization/Register")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [AuthIntegrationTest.Initializer::class])
internal class AuthIntegrationTest {
    class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            val mongoDBContainer = MongoDBContainer("mongo:4").withExposedPorts(27017)
            mongoDBContainer.start()
            val map: MutableMap<String, Any> = HashMap()
            map["spring.data.mongodb.host"] = mongoDBContainer.containerIpAddress
            map["spring.data.mongodb.port"] = mongoDBContainer.getMappedPort(27017)
            configurableApplicationContext.environment.propertySources.addLast(
                MapPropertySource("TestConfigProperties", map)
            )
        }
    }

    @Autowired
    private lateinit var restTemplateBuilder: RestTemplateBuilder

    @LocalServerPort
    private var port: Int = 0

    @Story("User login/register")
    @DisplayName("User can register and login")
    @Description("Check pipeline verify user can register, then login, then logout successfully")
    @Test
    fun `user can register and login`() {
        val restTemplate = restTemplateBuilder.build()

        val registerResponse = restTemplate.postForEntity(
            "http://localhost:$port/users/register?login=abab&age=2&password=1",
            null,
            Unit::class.java
        )

        assertThat(registerResponse.statusCode).isEqualTo(OK)

        val afterRegisterHeaders = HttpHeaders()
        afterRegisterHeaders["Cookie"] = registerResponse.headers.getValue("Set-Cookie")

        val afterRegisterMeResponse = restTemplate.exchange(
            "http://localhost:$port/me",
            GET,
            HttpEntity(null, afterRegisterHeaders),
            User::class.java
        )

        assertThat(afterRegisterMeResponse.statusCode).isEqualTo(OK)
        assertThat(afterRegisterMeResponse.body).isEqualTo(
            User(
                1,
                "abab",
                2,
                "S_USLzRFVMU73i67jNK349FgCtYxw4Wl18ziPHeFRZo="
            )
        )

        val logoutResponseStatus = restTemplate.exchange(
            "http://localhost:$port/users/logout",
            GET,
            HttpEntity(null, afterRegisterHeaders),
            Unit::class.java
        ).statusCode

        assertThat(logoutResponseStatus).isEqualTo(OK)

        val loginResponseStatus = restTemplate.exchange(
            "http://localhost:$port/users/login?login=abab&password=1",
            POST,
            HttpEntity(null, afterRegisterHeaders),
            Unit::class.java
        ).statusCode

        assertThat(loginResponseStatus).isEqualTo(OK)

        val afterLoginMeResponse = restTemplate.exchange(
            "http://localhost:$port/me",
            GET,
            HttpEntity(null, afterRegisterHeaders),
            User::class.java
        )

        assertThat(afterLoginMeResponse.statusCode).isEqualTo(OK)
        assertThat(afterLoginMeResponse.body).isEqualTo(
            User(
                1,
                "abab",
                2,
                "S_USLzRFVMU73i67jNK349FgCtYxw4Wl18ziPHeFRZo="
            )
        )
    }
}