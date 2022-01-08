package easy.soc.hacks.spring.e2e

import com.codeborne.selenide.Selenide.open
import com.codeborne.selenide.Selenide.title
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(SelenoidExtension::class)
class AuthTest {
    @Test
    fun `user can register`() {
        open("http://localhost:3000/")
        assertEquals(title(), "")

    }
}