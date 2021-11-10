package easy.soc.hacks.spring.domain

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Data
@Document(collection = "messages")
data class Message(
    @Id
    @JsonProperty("id")
    val id: Long,

    @DBRef
    @JsonSetter("from")
    val fromUser: User,

    @DBRef
    @JsonSetter("to")
    val toUser: User,

    @JsonProperty("message")
    val message: String
) {
    companion object {
        @Transient
        val SEQUENCE_NAME: String = "messages_sequence"
    }

    @JsonGetter("from")
    fun fromUserId(): Long = fromUser.id

    @JsonGetter("to")
    fun toUserId(): Long = toUser.id
}