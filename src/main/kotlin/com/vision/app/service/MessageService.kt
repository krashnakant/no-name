package com.vision.app.service

import com.vision.app.domain.AppUser
import com.vision.app.domain.Message
import com.vision.app.domain.dto.request.MessageResponseDto
import com.vision.app.repository.MessageRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class MessageService(
    private val messageRepository: MessageRepository
) :
    IBaseService<Message, String> {

    override fun <S : Message> save(entity: S): S {
        return messageRepository.save(entity)
    }

    fun findMessagesBySender(appUser: AppUser): MessageResponseDto {
        return toApiDto(message = messageRepository.findMessagesBySender(appUser).map { it.message })
    }

    fun findMessagesByReceiverAndSender(receiver: AppUser, sender: AppUser): MessageResponseDto {
        return toApiDto(
            message = messageRepository.findMessagesByReceiverAndSender(
                receiver = receiver,
                sender = sender
            ).map { it.message }
        )
    }

    fun findMessagesByReceiver(appUser: AppUser): MessageResponseDto {
        return toApiDto(message = messageRepository.findMessagesByReceiver(appUser).map { it.message })
    }

    private fun toApiDto(message: List<String>): MessageResponseDto {
        return MessageResponseDto(
            message = message
        )
    }

    override fun findById(id: String): Message? {
        return messageRepository.findByIdOrNull(id)
    }
}
