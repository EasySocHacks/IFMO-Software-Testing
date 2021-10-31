package easy.soc.hacks.spring.form

import lombok.Data

@Data
data class RegisterUserForm (
    val login: String,
    val age: Int,
    val password: String
)