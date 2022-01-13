package e2e

import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Selectors.*
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.Selenide.open
import io.qameta.allure.Description
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@Epic("e2e test")
@Feature("Auth")
@ExtendWith(SelenoidExtension::class)
class AuthTest {
    @Story("User register and login")
    @DisplayName("User can Auth")
    @Description("User can register and then login")
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