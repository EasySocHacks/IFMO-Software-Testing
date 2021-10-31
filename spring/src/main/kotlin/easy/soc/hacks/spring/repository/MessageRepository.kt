package easy.soc.hacks.spring.repository

import easy.soc.hacks.spring.domain.Message
import easy.soc.hacks.spring.domain.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository: MongoRepository<Message, Long> {
    fun findAllByFromUserAndToUser(fromUser: User, toUser: User): List<Message>
}