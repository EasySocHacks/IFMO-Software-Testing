package easy.soc.hacks.spring.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Data
@Document(collection = "users")
data class User (
    @Id
    @JsonProperty("id")
    var id: Long,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("age")
    val age: Int,

    @JsonProperty("password")
    val password: String,

    @DBRef
    @JsonIgnore
    val friends: MutableSet<User> = mutableSetOf()
) {
    companion object {
        @Transient
        val SEQUENCE_NAME: String = "users_sequence"
    }
}
