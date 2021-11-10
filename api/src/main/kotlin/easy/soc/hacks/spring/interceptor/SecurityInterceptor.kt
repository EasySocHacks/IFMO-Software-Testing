package easy.soc.hacks.spring.interceptor

import easy.soc.hacks.spring.annotation.WithSession
import easy.soc.hacks.spring.utils.session.Session
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class SecurityInterceptor: HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (!response.containsHeader("Access-Control-Allow-Origin"))
            response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000")
        if (!response.containsHeader("Access-Control-Allow-Credentials"))
            response.addHeader("Access-Control-Allow-Credentials", "true")
        if (!response.containsHeader("Cache-Control"))
            response.addHeader("Cache-Control", "no-store")

        if (handler is HandlerMethod && handler.hasMethodAnnotation(WithSession::class.java) &&
            Session.getUser(request.session) == null) {
            response.status = HttpStatus.FORBIDDEN.value()

            return false
        }

        return true
    }
}