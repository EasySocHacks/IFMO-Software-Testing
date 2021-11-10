package easy.soc.hacks.spring.domain

import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Data
@Document(collection = "database_sequences")
data class DatabaseSequence (
    @Id
    val id: String,

    val seq: Long = 0
)