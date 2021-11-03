package easy.soc.hacks.spring.service

import easy.soc.hacks.spring.domain.User
import easy.soc.hacks.spring.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.util.*
import javax.servlet.http.HttpSession

internal class UserServiceTest {
    private val userRepository = mock<UserRepository>()
    private val userService = UserService(userRepository)

    @AfterEach
    fun noMoreInteractions() {
        verifyNoMoreInteractions(userRepository)
    }

    @Test
    fun `findAll returns all presented users`() {
        val userList = listOf(
            User(1, "1", 1, "1"),
            User(2, "2", 2, "2"),
            User(3, "3", 3, "3"),
        )
        whenever(userRepository.findAllByOrderByIdDesc()).thenReturn(userList)

        assertThat(userService.findAll()).isEqualTo(userList)

        verify(userRepository).findAllByOrderByIdDesc()
    }

    @Test
    fun `findById returns same User`() {
        val user = User(1, "1", 1, "1")
        whenever(userRepository.findById(any())).thenReturn(Optional.of(user))

        assertThat(userService.findById(1)).isEqualTo(user)

        verify(userRepository).findById(1)
    }

    @Test
    fun `findById returns null on repository fail`() {
        whenever(userRepository.findById(any())).thenReturn(Optional.ofNullable(null))

        assertThat(userService.findById(1)).isEqualTo(null)

        verify(userRepository).findById(1)
    }

    @Test
    fun `register returns same User`() {
        val user = User(1, "1", 1, "1")
        whenever(userRepository.save(any())).thenReturn(user)

        assertThat(userService.register(user)).isEqualTo(user)

        verify(userRepository).save(user)
    }

    @Test
    fun `register returns null on repository fail`() {
        val user = User(1, "1", 1, "1")
        whenever(userRepository.save(any())).thenThrow(IllegalArgumentException::class.java)

        assertThat(userService.register(user)).isEqualTo(null)

        verify(userRepository).save(user)
    }

    @Test
    fun `login returns same User and set session attribute`() {
        val sessionAttributeStringBuilder = StringBuilder()
        val user = User(1, "1", 1, "1")
        val httpSession = mock<HttpSession>()
        whenever(httpSession.setAttribute(any(), any())).then {
            sessionAttributeStringBuilder.append(it.getArgument(1) as Long)
        }
        whenever(userRepository.findByNameAndPassword(any(), any())).thenReturn(Optional.of(user))

        assertThat(userService.login(user.name, user.password, httpSession)).isEqualTo(user)

        verify(userRepository).findByNameAndPassword(user.name, user.password)
        verify(httpSession).setAttribute("userId", 1L)
        verifyNoMoreInteractions(httpSession)
    }

    @Test
    fun `login returns same null on repository fail`() {
        val user = User(1, "1", 1, "1")
        val httpSession = mock<HttpSession>()
        whenever(userRepository.findByNameAndPassword(any(), any())).thenReturn(Optional.ofNullable(null))

        assertThat(userService.login(user.name, user.password, httpSession)).isEqualTo(null)

        verify(userRepository).findByNameAndPassword(user.name, user.password)
        verifyNoMoreInteractions(httpSession)
    }

    @Test
    fun `deregister returns same User`() {
        val user = User(1, "1", 1, "1")
        whenever(userRepository.findById(any())).thenReturn(Optional.of(user))

        assertThat(userService.deregister(user.id)).isEqualTo(user)

        verify(userRepository).findById(user.id)
        verify(userRepository).delete(user)
    }

    @Test
    fun `deregister returns null on repository fail`() {
        val user = User(1, "1", 1, "1")
        whenever(userRepository.findById(any())).thenReturn(Optional.ofNullable(null))

        assertThat(userService.deregister(user.id)).isEqualTo(null)

        verify(userRepository).findById(user.id)
    }

    @Test
    fun `getFriends returns same User list`() {
        val userList = listOf(
            User(2, "2", 2, "2"),
            User(3, "3", 3, "3"),
        )
        val user = User(1, "1", 1, "1", friends = mutableSetOf(2L, 3L))
        whenever(userRepository.findById(1)).thenReturn(Optional.of(user))
        whenever(userRepository.findById(2)).thenReturn(Optional.of(userList[0]))
        whenever(userRepository.findById(3)).thenReturn(Optional.of(userList[1]))

        assertThat(userService.getFriends(1)).isEqualTo(userList)

        verify(userRepository).findById(1)
        verify(userRepository).findById(2)
        verify(userRepository).findById(3)
    }

    @Test
    fun `getFriends returns null with no such user`() {
        whenever(userRepository.findById(any())).thenReturn(Optional.ofNullable(null))

        assertThat(userService.getFriends(1)).isEqualTo(null)

        verify(userRepository).findById(1)
    }

    @Test
    fun `getFriendById returns same User list`() {
        val userList = listOf(
            User(2, "2", 2, "2"),
            User(3, "3", 3, "3"),
        )
        val user = User(1, "1", 1, "1", friends = mutableSetOf(2L, 3L))
        whenever(userRepository.findById(1)).thenReturn(Optional.of(user))
        whenever(userRepository.findById(2)).thenReturn(Optional.of(userList[0]))
        whenever(userRepository.findById(3)).thenReturn(Optional.of(userList[1]))

        assertThat(userService.getFriendById(1, 2)).isEqualTo(userList[0])

        verify(userRepository).findById(1)
        verify(userRepository).findById(2)
        verify(userRepository).findById(3)
    }

    @Test
    fun `getFriendById returns null with no such user`() {
        whenever(userRepository.findById(any())).thenReturn(Optional.ofNullable(null))

        assertThat(userService.getFriendById(1, 2)).isEqualTo(null)

        verify(userRepository).findById(1)
    }

    @Test
    fun addFriend() {
        val firstUser = User(1, "1", 1, "1")
        val secondUser = User(2, "2", 2, "2")

        userService.addFriend(firstUser, secondUser)

        assertThat(firstUser.friends).isEqualTo(mutableSetOf(2L))
        assertThat(secondUser.friends).isEqualTo(mutableSetOf(1L))

        verify(userRepository).save(firstUser)
        verify(userRepository).save(secondUser)
    }

    @Test
    fun removeFriend() {
        val firstUser = User(1, "1", 1, "1", friends = mutableSetOf(2L))
        val secondUser = User(2, "2", 2, "2", friends = mutableSetOf(1L))

        userService.removeFriend(firstUser, secondUser)

        assertThat(firstUser.friends).isEqualTo(mutableSetOf<Long>())
        assertThat(secondUser.friends).isEqualTo(mutableSetOf<Long>())

        verify(userRepository).save(firstUser)
        verify(userRepository).save(secondUser)
    }
}