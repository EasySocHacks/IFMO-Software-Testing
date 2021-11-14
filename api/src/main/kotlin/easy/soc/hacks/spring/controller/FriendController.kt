package easy.soc.hacks.spring.controller

import easy.soc.hacks.spring.annotation.WithSession
import easy.soc.hacks.spring.domain.User
import easy.soc.hacks.spring.service.UserService
import easy.soc.hacks.spring.utils.session.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpSession

@RestController
class FriendController(
    @Autowired private val userService: UserService
) {
    @WithSession
    @GetMapping("/friends")
    fun friends(httpSession: HttpSession): ResponseEntity<List<User>> {
        val friends = userService.getFriends(Session.getUser(httpSession)!!) ?: return ResponseEntity(OK)

        return ResponseEntity(friends, OK)
    }

    @WithSession
    @GetMapping("/friends/nonFriendUsers")
    fun nonFriendUsers(httpSession: HttpSession): ResponseEntity<List<User>> {
        val user = userService.findById(Session.getUser(httpSession)!!) ?: return ResponseEntity(OK)

        return ResponseEntity(userService.findAll().minus(user.friends.mapNotNull {
            userService.findById(it)
        }.toSet()).minus(user), OK)
    }

    @WithSession
    @PostMapping("/friends/add/{id}")
    fun addFriend(@PathVariable id: Long, httpSession: HttpSession): ResponseEntity<Unit> {
        val userFrom = userService.findById(Session.getUser(httpSession)!!) ?: return ResponseEntity(NOT_FOUND)
        val userTo = userService.findById(id) ?: return ResponseEntity(NOT_FOUND)

        userService.addFriend(
            userFrom,
            userTo
        )

        return ResponseEntity(OK)
    }

    @WithSession
    @PostMapping("/friends/remove/{id}")
    fun removeFriend(@PathVariable id: Long, httpSession: HttpSession): ResponseEntity<Unit> {
        val userFrom = userService.findById(Session.getUser(httpSession)!!) ?: return ResponseEntity(NOT_FOUND)
        val userTo = userService.findById(id) ?: return ResponseEntity(NOT_FOUND)

        userService.removeFriend(
            userFrom,
            userTo
        )

        return ResponseEntity(OK)
    }

    @WithSession
    @GetMapping("/friend")
    fun friend(@RequestParam id: Long, httpSession: HttpSession): ResponseEntity<User> {
        val friend =
            userService.getFriendById(Session.getUser(httpSession)!!, id) ?: return ResponseEntity(OK)

        return ResponseEntity(friend, OK)
    }
}