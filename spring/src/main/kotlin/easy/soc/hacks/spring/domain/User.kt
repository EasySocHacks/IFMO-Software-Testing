package easy.soc.hacks.spring.domain

import lombok.Data
import org.springframework.data.annotation.Id

@Data
data class User (
    @Id
    val id: Long,
    val name: String,
    val age: Int,
    val password: String
)
