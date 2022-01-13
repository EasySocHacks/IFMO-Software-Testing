package easy.soc.hacks.spring.controller

import easy.soc.hacks.spring.TestHelper
import easy.soc.hacks.spring.domain.Message
import easy.soc.hacks.spring.domain.User
import easy.soc.hacks.spring.form.SendMessageForm
import easy.soc.hacks.spring.service.MessageService
import easy.soc.hacks.spring.service.SequenceGeneratorService
import easy.soc.hacks.spring.service.UserService
import easy.soc.hacks.spring.utils.session.Session
import io.qameta.allure.Description
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@Epic("Component test")
@Feature("Chat")
@WebMvcTest(ChatController::class)
internal class ChatControllerComponentTest {
    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var messageService: MessageService

    @MockBean
    private lateinit var sequenceGeneratorService: SequenceGeneratorService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Story("Get chat messages")
    @DisplayName("Chat mapping returns 'FORNIDDEN' on no session set")
    @Description("Chat messages request returns 'FORBIDDEN' due to not login/register user")
    @Test
    fun `chat mapping returns FORNIDDEN on no session set`() {
        mockMvc.get("/chat")
            .andExpect {
                status { isForbidden() }
            }
    }

    @Story("Get chat messages")
    @DisplayName("Chat mapping returns 'NOT_FOUND' on no user found")
    @Description("Chat messages request returns 'NOT_FOUND' due to companion user not found in database")
    @Test
    fun `chat mapping returns NOT_FOUND on no user found`() {
        given(userService.findById(any())).willReturn(null)

        mockMvc.get("/chat") {
            sessionAttr(Session.userIdSessionKey, 1L)
            param("id", "2")
        }.andExpect {
            status { isNotFound() }
        }

        verify(userService).findById(1L)
    }

    @Story("Get chat messages")
    @DisplayName("Chat mapping returns json chat message list")
    @Description("Chat messages request returns correct message list representing as json")
    @Test
    fun `chat mapping returns json chat message list`() {
        val fromUser = User(1, "1", 1, "1")
        val toUser = User(2, "2", 2, "2")
        val messageList = listOf(
            Message(1L, fromUser, toUser, "1"),
            Message(1L, fromUser, toUser, "2"),
            Message(1L, toUser, fromUser, "3"),
        )

        given(userService.findById(1L)).willReturn(fromUser)
        given(userService.findById(2L)).willReturn(toUser)
        given(messageService.getMessagesBetween(fromUser, toUser)).willReturn(messageList)

        mockMvc.get("/chat") {
            sessionAttr(Session.userIdSessionKey, 1L)
            param("id", "2")
        }.andExpect {
            status { isOk() }
            content { json(TestHelper.getMessageListJson(messageList)) }
        }

        verify(userService).findById(1L)
        verify(userService).findById(2L)
        verify(messageService).getMessagesBetween(fromUser, toUser)
    }

    @Story("Send chat messages")
    @DisplayName("SendMessage mapping returns 'FORNIDDEN' on no session set")
    @Description("Send messages request returns 'FORNIDDEN' due to not login/register user")
    @Test
    fun `sendMessage mapping returns FORNIDDEN on no session set`() {
        mockMvc.post("/sendMessage")
            .andExpect {
                status { isForbidden() }
            }
    }

    @Story("Send chat messages")
    @DisplayName("SendMessage mapping returns 'NOT_FOUND' on no user found")
    @Description("Send messages request returns 'NOT_FOUND' due to companion user not found in database")
    @Test
    fun `sendMessage mapping returns NOT_FOUND on no user found`() {
        given(userService.findById(any())).willReturn(null)

        mockMvc.post("/sendMessage") {
            sessionAttr(Session.userIdSessionKey, 1L)
            requestAttr("send-message", SendMessageForm::class)
            param("message", "message")
            param("receiver", "2")
        }.andExpect {
            status { isNotFound() }
        }

        verify(userService).findById(1L)
    }

    @Story("Send chat messages")
    @DisplayName("SendMessage mapping returns OK")
    @Description("Send messages request verify sending correct message to correct companion user. And then send OK")
    @Test
    fun `sendMessage mapping returns OK`() {
        val fromUser = User(1, "1", 1, "1")
        val toUser = User(2, "2", 2, "2")
        val message = Message(1L, fromUser, toUser, "1")

        given(userService.findById(1L)).willReturn(fromUser)
        given(userService.findById(2L)).willReturn(toUser)
        given(messageService.sendMessage(any())).willReturn(message)

        mockMvc.post("/sendMessage") {
            sessionAttr(Session.userIdSessionKey, 1L)
            requestAttr("send-message", SendMessageForm::class)
            param("message", "message")
            param("receiver", "2")
        }.andExpect {
            status { isOk() }
        }

        verify(userService).findById(1L)
        verify(userService).findById(2L)
    }

}