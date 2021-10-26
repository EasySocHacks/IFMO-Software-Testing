package easy.soc.hacks.spring.repository

import easy.soc.hacks.spring.domain.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: MongoRepository<User, Long> {
    fun findAllByOrderByIdDesc(): List<User>
}