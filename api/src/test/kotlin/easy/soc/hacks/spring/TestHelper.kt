package easy.soc.hacks.spring

import easy.soc.hacks.spring.domain.Message
import easy.soc.hacks.spring.domain.User

object TestHelper {
    fun getUserJson(user: User): String {
        return (
                "{" +
                        "\"id\": ${user.id}, " +
                        "\"name\": \"${user.name}\", " +
                        "\"age\": ${user.age}, " +
                        "\"password\": \"${user.getPasswordSha()}\", " +
                        "\"friends\": ${
                            user.getFriendList().joinToString(
                                prefix = "[",
                                postfix = "]",
                                separator = ", "
                            )
                        }}"
                )
    }

    fun getUserListJson(userList: List<User>): String {
        return userList.joinToString(
            prefix = "[",
            postfix = "]",
            separator = ", "
        ) { user -> getUserJson(user) }
    }

    fun getMessageJson(message: Message): String {
        return (
                "{" +
                        "\"id\": ${message.id}, " +
                        "\"from\": ${message.fromUser.id}, " +
                        "\"to\": ${message.toUser.id}, " +
                        "\"message\": \"${message.message}\"" +
                        "}"
                )
    }

    fun getMessageListJson(messageList: List<Message>): String {
        return messageList.joinToString(
            prefix = "[",
            postfix = "]",
            separator = ", "
        ) { message -> getMessageJson(message) }
    }
}