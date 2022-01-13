package e2e

import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Selectors.*
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.Selenide.open
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(SelenoidExtension::class)
class AuthTest {
    @Test
    fun `user can auth`() {
        open("register")
        `$`(byName("login")).value = "abacaba"
        `$`(byName("age")).value = "123"
        `$`(byName("password")).value = "222"
        `$`(byValue("Register")).click()

        open("login")
        `$`(byName("login")).value = "abacaba"
        `$`(byName("password")).value = "222"
        `$`(byValue("Login")).click()
    }

    @Test
    fun `page contains metadata`() {
        open("")
        `$`(byClassName("footer")).shouldHave(text("Created by EasySocHacks"))
    }
}