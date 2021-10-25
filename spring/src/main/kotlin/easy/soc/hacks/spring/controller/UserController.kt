package easy.soc.hacks.spring.controller

import easy.soc.hacks.spring.domain.User
import easy.soc.hacks.spring.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    @Autowired private val userService: UserService
) {
    @GetMapping("/users/all", "/users/", "/users")
    fun all(): List<User> {
        return userService.findAll()
    }

    @PostMapping("/users/add")
    fun add(@RequestBody user: User): String {
        userService.register(user)

        return "OK"
    }

    @PostMapping("/users/delete/{id}")
    fun delete(@PathVariable id: Long) {
        return userService.deregister(id)
    }
}