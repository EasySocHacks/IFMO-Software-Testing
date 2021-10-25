package easy.soc.hacks.spring.configuration

import easy.soc.hacks.spring.interceptor.SecurityInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Description
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class Configuration: WebMvcConfigurer {

    @Description("Add security interceptor")
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(SecurityInterceptor())
    }
}