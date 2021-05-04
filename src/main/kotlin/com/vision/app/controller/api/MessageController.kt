package com.vision.app.controller.api

import com.vision.app.domain.Message
import com.vision.app.domain.dto.request.MessageRequestDto
import com.vision.app.service.JsonConversionService
import com.vision.app.service.MessageService
import com.vision.app.service.UserService
import com.vision.app.service.kafka.KafkaProducer
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/message")
class MessageController(
    val kafkaProducer: KafkaProducer,
    val userService: UserService,
    val messageService: MessageService,
    val jsonConversionService: JsonConversionService
) {

    @PostMapping("/", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun messageSent(
        @RequestHeader("nick-name") nickName: String,
        @RequestBody messageRequestDto: MessageRequestDto
    ): ResponseEntity<*> {
        return if (nickName != messageRequestDto.to) {
            val users = userService.findUserIn(listOf(nickName, messageRequestDto.to))
            val message = Message(
                sender = users.first { it.nickName == nickName },
                receiver = users.first { it.nickName == messageRequestDto.to },
                message = messageRequestDto.message
            )
            jsonConversionService.mapToJson(message)?.let { kafkaProducer.publish(message = it) }

            ResponseEntity.ok(HttpStatus.ACCEPTED)
        } else {
            ResponseEntity("sending message to self is not supported yet", HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping(
        "/received",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getReceivedMessage(@RequestHeader("nick-name") nickName: String): ResponseEntity<*> {
        val user = userService.findByNickName(nickName = nickName)
        return ResponseEntity.ok(messageService.findMessagesByReceiver(user))
    }

    @GetMapping(
        "/sent",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getSentMessage(@RequestHeader("nick-name") nickName: String): ResponseEntity<*> {
        val user = userService.findByNickName(nickName = nickName)
        return ResponseEntity.ok(messageService.findMessagesBySender(user))
    }

    @GetMapping(
        "/search/{user}",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun searchMessage(@RequestHeader("nick-name") nickName: String, @PathVariable user: String): ResponseEntity<*> {
        val users = userService.findUserIn(listOf(nickName, user))
        return ResponseEntity.ok(
            messageService.findMessagesByReceiverAndSender(
                users.first { it.nickName == nickName },
                users.first { it.nickName == user }
            )
        )
    }
}
