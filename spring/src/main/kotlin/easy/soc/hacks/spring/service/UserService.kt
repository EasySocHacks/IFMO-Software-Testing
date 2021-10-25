package easy.soc.hacks.spring.service

import easy.soc.hacks.spring.domain.User
import easy.soc.hacks.spring.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired private val userRepository: UserRepository
) {
    fun findAll(): List<User> {
        return userRepository.findAllByOrderByIdDesc()
    }

    fun register(user: User): User {
        return userRepository.save(user)
    }

    fun deregister(id: Long) {
        return userRepository.delete(userRepository.findById(id).get())
    }
}