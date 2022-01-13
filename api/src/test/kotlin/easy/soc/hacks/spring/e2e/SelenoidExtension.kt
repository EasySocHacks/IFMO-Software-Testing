package e2e

import com.codeborne.selenide.Configuration
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.openqa.selenium.chrome.ChromeOptions

class SelenoidExtension : BeforeAllCallback {
    override fun beforeAll(extensionContext: ExtensionContext) {
        Configuration.remote = "http://localhost:4444/wd/hub"
        Configuration.driverManagerEnabled = false
        Configuration.baseUrl = "http://localhost:3000/"

        val options: MutableMap<String, Boolean> = HashMap()
        options["enableVNC"] = true

        Configuration.browserCapabilities = ChromeOptions()
        Configuration.browserCapabilities.setCapability("selenoid:options", options)
    }
}