package easy.soc.hacks.spring.domain

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.security.MessageDigest
import java.util.*

@Data
@Document(collection = "users")
data class User(
    @Id
    @JsonProperty("id")
    var id: Long,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("age")
    val age: Int,

    @JsonSetter("password")
    val password: String,

    @JsonSetter("friends")
    val friends: MutableSet<Long> = mutableSetOf(),
) {
    companion object {
        @Transient
        val SEQUENCE_NAME: String = "users_sequence"
    }

    @JsonGetter("password")
    fun getPasswordSha(): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(password.toByte())
        val digest: ByteArray = messageDigest.digest()
        return Base64.getUrlEncoder().encodeToString(digest)
    }

    @JsonGetter("friends")
    fun getFriendList(): List<Long> = friends.toList()
}
