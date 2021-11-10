package easy.soc.hacks.spring.service

import easy.soc.hacks.spring.domain.Message
import easy.soc.hacks.spring.domain.User
import easy.soc.hacks.spring.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MessageService(
    @Autowired private val messageRepository: MessageRepository
) {
    fun getMessagesBetween(fromUser: User, toUser: User): List<Message> {
        return messageRepository.findAllByFromUserAndToUser(fromUser, toUser)
            .plus(messageRepository.findAllByFromUserAndToUser(toUser, fromUser))
    }

    fun sendMessage(message: Message): Message? {
        return try {
            messageRepository.save(message)
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}