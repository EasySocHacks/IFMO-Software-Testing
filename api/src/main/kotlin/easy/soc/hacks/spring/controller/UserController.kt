package easy.soc.hacks.spring.controller

import easy.soc.hacks.spring.annotation.WithSession
import easy.soc.hacks.spring.domain.User
import easy.soc.hacks.spring.form.LoginUserForm
import easy.soc.hacks.spring.form.RegisterUserForm
import easy.soc.hacks.spring.service.SequenceGeneratorService
import easy.soc.hacks.spring.service.UserService
import easy.soc.hacks.spring.utils.session.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpSession

@RestController
class UserController(
    @Autowired private val userService: UserService,
    @Autowired private val sequenceGeneratorService: SequenceGeneratorService
) {
    @GetMapping("/me")
    fun me(httpSession: HttpSession): ResponseEntity<User> {
        val userId = Session.getUser(httpSession) ?: return ResponseEntity(OK)
        val user = userService.findById(userId) ?: return ResponseEntity(OK)

        return ResponseEntity(user, OK)
    }

    @GetMapping("/users/all", "/users")
    fun users(): List<User> {
        return userService.findAll()
    }

    @PostMapping("/users/register")
    fun register(@ModelAttribute("register") registerUserForm: RegisterUserForm, httpSession: HttpSession): ResponseEntity<Unit> {
        val user = User(
            sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME),
            registerUserForm.login,
            registerUserForm.age,
            registerUserForm.password
        )
        userService.register(user) ?: return ResponseEntity(NOT_ACCEPTABLE)
        Session.setUser(httpSession, user)

        return ResponseEntity(OK)
    }

    @PostMapping("/users/login")
    fun login(@ModelAttribute("login") loginUserForm: LoginUserForm, httpSession: HttpSession): ResponseEntity<Unit> {
        userService.login(loginUserForm.login, loginUserForm.password, httpSession) ?: return ResponseEntity(NOT_FOUND)

        return ResponseEntity(OK)
    }

    @WithSession
    @GetMapping("/users/logout")
    fun logout(httpSession: HttpSession): ResponseEntity<Unit> {
        Session.unsetUser(httpSession)

        return ResponseEntity(OK)
    }

    @PostMapping("/users/delete/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        userService.deregister(id) ?: return ResponseEntity(NOT_FOUND)

        return ResponseEntity(OK)
    }
}