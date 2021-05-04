package com.vision.app.service

import com.vision.app.domain.AppUser
import com.vision.app.domain.Message
import com.vision.app.domain.dto.request.MessageResponseDto
import com.vision.app.repository.MessageRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MessageServiceTest {

    private val messageRepository: MessageRepository = mockk(relaxed = true)
    private val messageService = MessageService(messageRepository = messageRepository)

    @Test
    fun `Given message input when input is valid then save into db`() {
        val message = Message(sender = AppUser1, receiver = AppUser2, message = "Test message")

        every { messageRepository.save(message) } returns message
        messageService.save(message)

        verify(atLeast = 1) { messageRepository.save(message) }
    }

    @Test
    fun `Given input appUser then return sender list of message`() {

        every { messageRepository.findMessagesBySender(AppUser1) } returns listOf(
            Message(
                sender = AppUser1,
                receiver = AppUser2,
                message = "Test message"
            )
        )

        val result = messageService.findMessagesBySender(AppUser1)
        val expected = MessageResponseDto(message = listOf("Test message"))
        assertEquals(expected, result)
    }

    @Test
    fun `Given input receiver and sender app User then return MessageResponseDto with list of message`() {

        every { messageRepository.findMessagesByReceiverAndSender(AppUser1, AppUser2) } returns listOf(
            Message(
                sender = AppUser1,
                receiver = AppUser2,
                message = "Test message"
            )
        )

        val result = messageService.findMessagesByReceiverAndSender(AppUser1, AppUser2)
        val expected = MessageResponseDto(message = listOf("Test message"))

        assertEquals(expected, result)
    }

    companion object {
        private val AppUser1 = AppUser(nickName = "user1")
        private val AppUser2 = AppUser(nickName = "user2")
    }
}
