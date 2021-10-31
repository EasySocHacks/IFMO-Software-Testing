package easy.soc.hacks.spring.controller

import easy.soc.hacks.spring.annotation.WithSession
import easy.soc.hacks.spring.domain.User
import easy.soc.hacks.spring.service.UserService
import easy.soc.hacks.spring.utils.session.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpSession

@RestController
class FriendController(
    @Autowired private val userService: UserService
) {
    @WithSession
    @GetMapping("/friends")
    fun friends(httpSession: HttpSession): List<User>? {
        return userService.getFriends(Session.getUser(httpSession)!!)
    }

    @WithSession
    @PostMapping("/friends/add/{id}")
    fun addFriend(@PathVariable id: Long, httpSession: HttpSession): HttpStatus {
        val userFrom = userService.findById(Session.getUser(httpSession)!!) ?: return NOT_FOUND
        val userTo = userService.findById(id) ?: return NOT_FOUND

        userService.addFriend(
            userFrom,
            userTo
        )

        return OK
    }

    @WithSession
    @PostMapping("/friends/remove/{id}")
    fun removeFriend(@PathVariable id: Long, httpSession: HttpSession): HttpStatus {
        val userFrom = userService.findById(Session.getUser(httpSession)!!) ?: return NOT_FOUND
        val userTo = userService.findById(id) ?: return NOT_FOUND

        userService.removeFriend(
            userFrom,
            userTo
        )

        return OK
    }

    @WithSession
    @GetMapping("/friend")
    fun friend(@RequestParam id: Long, httpSession: HttpSession): User? {
        return userService.getFriendById(Session.getUser(httpSession)!!, id)
    }
}