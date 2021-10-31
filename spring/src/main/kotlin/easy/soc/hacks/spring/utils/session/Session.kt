package easy.soc.hacks.spring.utils.session

import easy.soc.hacks.spring.domain.User
import javax.servlet.http.HttpSession

object Session {
    private const val userIdSessionKey = "userId"

    fun setUser(httpSession: HttpSession, user: User) {
        httpSession.setAttribute(userIdSessionKey, user.id)
    }

    fun unsetUser(httpSession: HttpSession) {
        httpSession.removeAttribute(userIdSessionKey)
    }

    fun getUser(httpSession: HttpSession): Long? {
        return httpSession.getAttribute(userIdSessionKey) as Long?
    }
}