package easy.soc.hacks.spring.form

import lombok.Data

@Data
data class LoginUserForm (
    val login: String,
    val password: String
)