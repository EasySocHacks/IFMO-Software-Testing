package easy.soc.hacks.spring.controller

import easy.soc.hacks.spring.annotation.WithSession
import easy.soc.hacks.spring.domain.Message
import easy.soc.hacks.spring.form.SendMessageForm
import easy.soc.hacks.spring.service.MessageService
import easy.soc.hacks.spring.service.SequenceGeneratorService
import easy.soc.hacks.spring.service.UserService
import easy.soc.hacks.spring.utils.session.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpSession

@RestController
class ChatController(
    @Autowired private val userService: UserService,
    @Autowired private val messageService: MessageService,
    @Autowired private val sequenceGeneratorService: SequenceGeneratorService
) {
    @WithSession
    @GetMapping("/chat")
    fun chat(@RequestParam id: Long, httpSession: HttpSession): ResponseEntity<Any> {
        val fromUser = userService.findById(Session.getUser(httpSession)!!) ?: return ResponseEntity(emptyList<Message>(),  NOT_FOUND)
        val toUser = userService.findById(id) ?: return ResponseEntity(emptyList<Message>(),  NOT_FOUND)

        return ResponseEntity(messageService.getMessagesBetween(fromUser, toUser), OK)
    }

    @WithSession
    @PostMapping("/sendMessage")
    fun sendMessage(@ModelAttribute("send-message") sendMessageForm: SendMessageForm, httpSession: HttpSession): ResponseEntity<Unit> {
        val fromUser = userService.findById(Session.getUser(httpSession)!!) ?: return ResponseEntity(NOT_FOUND)
        val toUser = userService.findById(sendMessageForm.receiver) ?: return ResponseEntity(NOT_FOUND)

        val message = Message(
            sequenceGeneratorService.generateSequence(Message.SEQUENCE_NAME),
            fromUser,
            toUser,
            sendMessageForm.message
        )

        messageService.sendMessage(message)

        return ResponseEntity(OK)
    }
}