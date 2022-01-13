package easy.soc.hacks.spring.intergation

import io.qameta.allure.*
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
@Feature("Message exchange")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [MessagingIntegrationTest.Initializer::class])
class MessagingIntegrationTest {
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

    @Story("Send/receive message")
    @DisplayName("Users can write messages to each other")
    @Description(
        "Check pipeline verify user1 can write to user2, then user2 can read newbie message, " +
                "then user2 can write to user1, then user1 can read both newbie messages"
    )
    @Flaky
    @Test
    fun `users can write messages to each other`() {
        val restTemplate = restTemplateBuilder.build()

        val aUserRegisterResponse = restTemplate.postForEntity(
            "http://localhost:$port/users/register?login=a&age=1&password=1",
            null,
            Unit::class.java
        )

        assertThat(aUserRegisterResponse.statusCode).isEqualTo(OK)

        val bUserRegisterResponse = restTemplate.postForEntity(
            "http://localhost:$port/users/register?login=b&age=1&password=1",
            null,
            Unit::class.java
        )

        assertThat(bUserRegisterResponse.statusCode).isEqualTo(OK)

        val aUserHeaders = HttpHeaders()
        aUserHeaders["Cookie"] = aUserRegisterResponse.headers.getValue("Set-Cookie")

        val bUserHeaders = HttpHeaders()
        bUserHeaders["Cookie"] = bUserRegisterResponse.headers.getValue("Set-Cookie")

        val aUserSetBAsFriend = restTemplate.exchange(
            "http://localhost:$port/friends/add/2",
            POST,
            HttpEntity(null, aUserHeaders),
            Unit::class.java
        )

        assertThat(aUserSetBAsFriend.statusCode).isEqualTo(OK)

        val aUserSendMessageResponseStatus = restTemplate.exchange(
            "http://localhost:$port/sendMessage?message=a_to_b&receiver=2",
            POST,
            HttpEntity(null, aUserHeaders),
            Unit::class.java
        ).statusCode

        val bUserSendMessageResponseStatus = restTemplate.exchange(
            "http://localhost:$port/sendMessage?message=b_to_a&receiver=1",
            POST,
            HttpEntity(null, bUserHeaders),
            Unit::class.java
        ).statusCode

        assertThat(aUserSendMessageResponseStatus).isEqualTo(OK)
        assertThat(bUserSendMessageResponseStatus).isEqualTo(OK)

        val aUserCharWithBResponse = restTemplate.exchange(
            "http://localhost:$port/chat?id=2",
            GET,
            HttpEntity(null, aUserHeaders),
            List::class.java
        )

        val bUserCharWithAResponse = restTemplate.exchange(
            "http://localhost:$port/chat?id=1",
            GET,
            HttpEntity(null, bUserHeaders),
            List::class.java
        )

        assertThat(aUserCharWithBResponse.statusCode).isEqualTo(OK)
        assertThat(bUserCharWithAResponse.statusCode).isEqualTo(OK)

        assertThat(aUserCharWithBResponse.body?.size).isEqualTo(2)
        assertThat(aUserCharWithBResponse.body?.get(0).toString()).isEqualTo("{id=1, from=1, to=2, message=a_to_b}")
        assertThat(aUserCharWithBResponse.body?.get(1).toString()).isEqualTo("{id=2, from=2, to=1, message=b_to_a}")
    }
}