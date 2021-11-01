package easy.soc.hacks.spring.service

import easy.soc.hacks.spring.domain.User
import easy.soc.hacks.spring.repository.UserRepository
import easy.soc.hacks.spring.utils.session.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpSession

@Service
class UserService(
    @Autowired private val userRepository: UserRepository
) {
    fun findAll(): List<User> {
        return userRepository.findAllByOrderByIdDesc()
    }

    fun findById(id: Long): User? {
        return userRepository.findById(id).orElseGet { null }
    }

    fun register(user: User): User? {
        return try {
            userRepository.save(user)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    fun login(name: String, password: String, httpSession: HttpSession): User? {
        val user = userRepository.findByNameAndPassword(name, password).orElseGet { null } ?: return null

        Session.setUser(httpSession, user)

        return user
    }

    fun deregister(id: Long): User? {
        val user = userRepository.findById(id).orElseGet { null }
        userRepository.delete(user)

        return user
    }

    fun getFriends(id: Long): List<User>? {
        return try {
            userRepository.findById(id).get().friends.toList().mapNotNull {
                userRepository.findById(it).orElseGet { null }
            }
        } catch (e: NoSuchElementException) {
            null
        }
    }

    fun getFriendById(id: Long, friendId: Long): User? {
        return try {
            userRepository.findById(id).get().friends.mapNotNull {
                userRepository.findById(it).orElseGet { null }
            }.find { it.id == friendId }
        } catch (e: NoSuchElementException) {
            null
        }
    }

    fun addFriend(userFrom: User, userTo: User) {
        userFrom.friends.add(userTo.id)
        userTo.friends.add(userFrom.id)

        userRepository.save(userFrom)
        userRepository.save(userTo)
    }

    fun removeFriend(userFrom: User, userTo: User) {
        userFrom.friends.remove(userTo.id)
        userTo.friends.remove(userFrom.id)

        userRepository.save(userFrom)
        userRepository.save(userTo)
    }

    fun isFriend(userFrom: User, userTo: User): Boolean {
        return userFrom.friends.contains(userTo.id)
    }
}