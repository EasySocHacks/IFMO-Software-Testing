package easy.soc.hacks.spring.controller

import easy.soc.hacks.spring.TestHelper
import easy.soc.hacks.spring.domain.User
import easy.soc.hacks.spring.service.UserService
import easy.soc.hacks.spring.utils.session.Session
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

@WebMvcTest(FriendController::class)
internal class FriendControllerComponentTest {
    @MockBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `friends mapping returns FORBIDDEN on no session set`() {
        mockMvc.get("/friends")
            .andExpect {
                status { isForbidden() }
            }
    }

    @Test
    fun `friends mapping returns empty json on empty friend list`() {
        given(userService.getFriends(any())).willReturn(emptyList())

        mockMvc.get("/friends") {
            sessionAttr(Session.userIdSessionKey, 1L)
        }.andExpect {
            status { isOk() }
            content { json("[]") }
        }

        verify(userService).getFriends(1L)
    }

    @Test
    fun `friends mapping returns non-empty connect json on non-empty friend list`() {
        val userList = listOf(
            User(1, "1", 1, "1"),
            User(2, "2", 2, "2"),
            User(3, "3", 3, "3")
        )
        given(userService.getFriends(any())).willReturn(userList)

        mockMvc.get("/friends") {
            sessionAttr(Session.userIdSessionKey, 1L)
        }.andExpect {
            status { isOk() }
            content { json(TestHelper.getUserListJson(userList)) }
        }

        verify(userService).getFriends(1L)
    }

    @Test
    fun `nonFriendUsers mapping returns FORBIDDEN on no session set`() {
        mockMvc.get("/friends/nonFriendUsers")
            .andExpect {
                status { isForbidden() }
            }
    }

    @Test
    fun `nonFriendUsers mapping returns empty json on no current user set`() {
        given(userService.findById(any())).willReturn(null)

        mockMvc.get("/friends/nonFriendUsers") {
            sessionAttr(Session.userIdSessionKey, 1L)
        }.andExpect {
            status { isOk() }
        }

        verify(userService).findById(1L)
    }

    @Test
    fun `nonFriendUsers mapping returns non-empty connect json on non-empty friend list`() {
        val friendList = listOf(
            User(2, "2", 2, "2")
        )
        val user = User(1, "1", 1, "1", friends = mutableSetOf(2L))
        val nonFriend = User(3, "3", 3, "3")
        given(userService.findById(1L)).willReturn(user)
        given(userService.findById(2L)).willReturn(friendList.first())
        given(userService.findById(3L)).willReturn(nonFriend)
        given(userService.findAll()).willReturn(friendList.plus(user).plus(nonFriend))

        mockMvc.get("/friends/nonFriendUsers") {
            sessionAttr(Session.userIdSessionKey, 1L)
        }.andExpect {
            status { isOk() }
            content { json(TestHelper.getUserListJson(listOf(nonFriend))) }
        }

        verify(userService).findById(1L)
        verify(userService).findAll()
    }

    @Test
    fun `friends add mapping returns FORBIDDEN on no session set`() {
        mockMvc.post("/friends/add/1")
            .andExpect {
                status { isForbidden() }
            }
    }

    @Test
    fun `friends add mapping returns NOT_FOUND on no current user`() {
        given(userService.findById(any())).willReturn(null)

        mockMvc.post("/friends/add/1") {
            sessionAttr(Session.userIdSessionKey, 1L)
        }.andExpect {
            status { isNotFound() }
        }

        verify(userService).findById(1L)
    }

    @Test
    fun `friends add mapping returns OK`() {
        val fromUser = User(1, "1", 1, "1")
        val toUser = User(2, "2", 2, "2")
        given(userService.findById(1L)).willReturn(fromUser)
        given(userService.findById(2L)).willReturn(toUser)

        mockMvc.post("/friends/add/2") {
            sessionAttr(Session.userIdSessionKey, 1L)
        }.andExpect {
            status { isOk() }
        }

        verify(userService).findById(1L)
        verify(userService).findById(2L)
        verify(userService).addFriend(fromUser, toUser)
    }

    @Test
    fun `friends remove mapping returns FORBIDDEN on no session set`() {
        mockMvc.post("/friends/remove/1")
            .andExpect {
                status { isForbidden() }
            }
    }

    @Test
    fun `friends remove mapping returns NOT_FOUND on no current user`() {
        given(userService.findById(any())).willReturn(null)

        mockMvc.post("/friends/remove/1") {
            sessionAttr(Session.userIdSessionKey, 1L)
        }.andExpect {
            status { isNotFound() }
        }

        verify(userService).findById(1L)
    }

    @Test
    fun `friends remove mapping returns OK`() {
        val fromUser = User(1, "1", 1, "1")
        val toUser = User(2, "2", 2, "2")
        given(userService.findById(1L)).willReturn(fromUser)
        given(userService.findById(2L)).willReturn(toUser)

        mockMvc.post("/friends/remove/2") {
            sessionAttr(Session.userIdSessionKey, 1L)
        }.andExpect {
            status { isOk() }
        }

        verify(userService).findById(1L)
        verify(userService).findById(2L)
        verify(userService).removeFriend(fromUser, toUser)
    }

    @Test
    fun `friend mapping returns FORBIDDEN on no session set`() {
        mockMvc.get("/friend")
            .andExpect {
                status { isForbidden() }
            }
    }

    @Test
    fun `friend mapping returns empty json on no current user`() {
        given(userService.getFriendById(any(), any())).willReturn(null)

        mockMvc.get("/friend") {
            sessionAttr(Session.userIdSessionKey, 1L)
            param("id", "2")
        }.andExpect {
            status { isOk() }
        }

        verify(userService).getFriendById(1L, 2L)
    }

    @Test
    fun `friend mapping returns friend json`() {
        val friendUser = User(2, "2", 2, "2")
        given(userService.getFriendById(any(), any())).willReturn(friendUser)

        mockMvc.get("/friend") {
            sessionAttr(Session.userIdSessionKey, 1L)
            param("id", "2")
        }.andExpect {
            status { isOk() }
            content { json(TestHelper.getUserJson(friendUser)) }
        }

        verify(userService).getFriendById(1L, 2L)
    }
}