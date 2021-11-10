package easy.soc.hacks.spring.form

import lombok.Data

@Data
data class SendMessageForm (
    val message: String,
    val receiver: Long
)