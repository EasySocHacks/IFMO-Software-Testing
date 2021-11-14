package easy.soc.hacks.spring.service

import easy.soc.hacks.spring.domain.Message
import easy.soc.hacks.spring.domain.User
import easy.soc.hacks.spring.repository.MessageRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class MessageServiceTest {
    private val messageRepository = mock<MessageRepository>()
    private val messageService = MessageService(messageRepository)

    private val firstUser = User(1, "1", 1, "1")
    private val secondUser = User(2, "2", 2, "2")

    @Test
    fun `getMessagesBetween with empty message list`() {
        whenever(messageRepository.findAllByFromUserAndToUser(any(), any())).thenReturn(emptyList())

        assertThat(messageService.getMessagesBetween(
            firstUser,
            secondUser
        )).isEqualTo(emptyList<Message>())

        verify(messageRepository).findAllByFromUserAndToUser(firstUser, secondUser)
        verify(messageRepository).findAllByFromUserAndToUser(secondUser, firstUser)
    }

    @Test
    fun `getMessagesBetween with both users present messages`() {
        val firstUserMessageList = listOf(
            Message(1, firstUser, secondUser, "1"),
            Message(2, firstUser, secondUser, "2"),
            Message(3, firstUser, secondUser, "3")
        )
        val secondUserMessageList = listOf(
            Message(1, secondUser, firstUser, "1"),
            Message(2, secondUser, firstUser, "2"),
            Message(3, secondUser, firstUser, "3")
        )
        whenever(messageRepository.findAllByFromUserAndToUser(any(), any()))
            .thenReturn(firstUserMessageList)
            .thenReturn(secondUserMessageList)

        assertThat(messageService.getMessagesBetween(
            firstUser,
            secondUser
        )).isEqualTo(firstUserMessageList.plus(secondUserMessageList))

        verify(messageRepository).findAllByFromUserAndToUser(firstUser, secondUser)
        verify(messageRepository).findAllByFromUserAndToUser(secondUser, firstUser)
    }
}