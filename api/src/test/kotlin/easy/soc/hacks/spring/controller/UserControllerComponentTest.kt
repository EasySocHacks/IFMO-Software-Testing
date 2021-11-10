package easy.soc.hacks.spring.controller

import easy.soc.hacks.spring.TestHelper
import easy.soc.hacks.spring.domain.User
import easy.soc.hacks.spring.form.LoginUserForm
import easy.soc.hacks.spring.form.RegisterUserForm
import easy.soc.hacks.spring.service.SequenceGeneratorService
import easy.soc.hacks.spring.service.UserService
import easy.soc.hacks.spring.utils.session.Session
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.mock.web.MockHttpSession
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(UserController::class)
internal class UserControllerComponentTest {

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var sequenceGeneratorService: SequenceGeneratorService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `me mapping returns empty json on no user set in session`() {
        mockMvc.get("/me")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `me mapping returns empty json on user set in session but no current user in service`() {
        given(userService.findById(any())).willReturn(null)

        mockMvc.get("/me") {
            session = MockHttpSession().apply {
                setAttribute(Session.userIdSessionKey, 1L)
            }
        }.andExpect {
            status { isOk() }
        }

        verify(userService).findById(1L)
    }

    @Test
    fun `me mapping returns correct user json`() {
        val user = User(1L, "1", 1, "1")
        given(userService.findById(any())).willReturn(user)

        mockMvc.get("/me") {
            session = MockHttpSession().apply {
                setAttribute(Session.userIdSessionKey, 1L)
            }
        }.andExpect {
            status { isOk() }
            content {
                json(TestHelper.getUserJson(user))
            }
        }

        verify(userService).findById(1L)
    }

    @Test
    fun `users mapping returns all users`() {
        val userList = listOf(
            User(1L, "1", 1, "1"),
            User(1L, "1", 1, "1"),
            User(1L, "1", 1, "1")
        )
        given(userService.findAll()).willReturn(userList)

        mockMvc.get("/users")
            .andExpect {
                status { isOk() }
                content {
                    json(TestHelper.getUserListJson(userList))
                }
            }

        verify(userService).findAll()
    }

    @Test
    fun `register mapping returns NOT_ACCEPTABLE on registration fail`() {
        given(sequenceGeneratorService.generateSequence(any())).willReturn(1L)
        given(userService.register(any())).willReturn(null)

        mockMvc.post("/users/register") {
            requestAttr("register", RegisterUserForm::class)
            param("login", "login")
            param("age", "12")
            param("password", "password")
        }.andExpect {
            status { isNotAcceptable() }
        }

        verify(sequenceGeneratorService).generateSequence(User.SEQUENCE_NAME)
        verify(userService).register(User(1L, "login", 12, "password"))
    }

    @Test
    fun `register mapping returns OK on registration succeed`() {
        val user = User(1L, "login", 12, "password")

        given(sequenceGeneratorService.generateSequence(any())).willReturn(1L)
        given(userService.register(any())).willReturn(user)

        mockMvc.post("/users/register") {
            requestAttr("register", RegisterUserForm::class)
            param("login", "login")
            param("age", "12")
            param("password", "password")
        }.andExpect {
            status { isOk() }
        }

        verify(sequenceGeneratorService).generateSequence(User.SEQUENCE_NAME)
        verify(userService).register(user)
    }

    @Test
    fun `login mapping returns NOT_FOUND on login fail`() {
        given(userService.login(any(), any(), any())).willReturn(null)

        mockMvc.post("/users/login") {
            requestAttr("login", LoginUserForm::class)
            param("login", "login")
            param("password", "password")
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `login mapping returns OK on login succeed`() {
        val user = User(1L, "login", 12, "password")

        given(userService.login(any(), any(), any())).willReturn(user)

        mockMvc.post("/users/login") {
            requestAttr("login", LoginUserForm::class)
            param("login", "login")
            param("password", "password")
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `logout mapping returns OK`() {
        mockMvc.get("/users/logout") {
            session = MockHttpSession().apply {
                setAttribute(Session.userIdSessionKey, 1L)
            }
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `delete mapping returns NOT_FOUND on delete fail`() {
        given(userService.deregister(any())).willReturn(null)

        mockMvc.post("/users/delete/1")
            .andExpect {
                status { isNotFound() }
            }

        verify(userService).deregister(1L)
    }

    @Test
    fun `delete mapping returns OK on delete succeed`() {
        given(userService.deregister(any())).willReturn(User(1, "1", 1, "1"))

        mockMvc.post("/users/delete/1")
            .andExpect {
                status { isOk() }
            }

        verify(userService).deregister(1L)
    }
}