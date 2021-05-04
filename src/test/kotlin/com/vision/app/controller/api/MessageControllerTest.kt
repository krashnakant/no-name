package com.vision.app.controller.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.vision.app.domain.AppUser
import com.vision.app.domain.Message
import com.vision.app.domain.dto.request.MessageRequestDto
import com.vision.app.domain.dto.request.MessageResponseDto
import com.vision.app.service.JsonConversionService
import com.vision.app.service.MessageService
import com.vision.app.service.UserService
import com.vision.app.service.kafka.KafkaProducer
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.sql.Timestamp
import java.time.Instant

@WebMvcTest(MessageController::class)
internal class MessageControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var messageService: MessageService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var userService: UserService

    @MockkBean
    private lateinit var jsonConversionService: JsonConversionService

    @MockkBean
    private lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @MockkBean
    private lateinit var kafkaProducer: KafkaProducer

    @Test
    fun messageSentResultInOk() {
        every { userService.findUserIn(listOf(USER_PARAM_VALUE, "to")) } returns listOf(
            AppUser(nickName = USER_PARAM_VALUE), AppUser(nickName = "to")
        )
        every { kafkaProducer.publish(any()) } just runs
        every {
            jsonConversionService.mapToJson(any())
        } returns writeValueAsString(
            Message(
                messageId = null,
                sender = AppUser(id = null, nickName = USER_PARAM_VALUE),
                receiver = AppUser(id = null, nickName = "to"),
                message = "test message",
                timestamp = Timestamp.from(Instant.now())
            )
        )
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/message/").header(
                    HEADER_NAME, HEADER_VALUE
                ).contentType(APPLICATION_JSON_VALUE)
                    .content(writeValueAsString(MessageRequestDto(to = "to", message = "test message")))
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun getReceivedMessageResultInOk() {

        every {
            messageService.findMessagesByReceiver(
                AppUser(
                    id = null,
                    nickName = USER_PARAM_VALUE
                )
            )
        } returns MessageResponseDto(
            message = emptyList()
        )

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/message/received").header(
                HEADER_NAME, HEADER_VALUE
            ).contentType(APPLICATION_JSON_VALUE)

        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun getSentMessageResultInOk() {
        every {
            messageService.findMessagesBySender(
                AppUser(
                    id = null,
                    nickName = USER_PARAM_VALUE
                )
            )
        } returns MessageResponseDto(
            message = emptyList()
        )
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/message/sent").header(
                HEADER_NAME, HEADER_VALUE
            ).contentType(APPLICATION_JSON_VALUE)

        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun searchMessageResultInOk() {

        every {
            messageService.findMessagesByReceiverAndSender(
                AppUser(
                    id = null,
                    nickName = USER_PARAM_VALUE
                ),
                AppUser(
                    id = null,
                    nickName = USER_PARAM_NAME
                )
            )
        } returns MessageResponseDto(
            message = emptyList()
        )
        every { userService.findUserIn(any()) } returns listOf(
            AppUser(nickName = USER_PARAM_VALUE), AppUser(nickName = "user")
        )

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/message/search/{user}", "user").header(
                HEADER_NAME, HEADER_VALUE
            ).param(USER_PARAM_NAME, USER_PARAM_VALUE).contentType(APPLICATION_JSON_VALUE)

        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @BeforeEach
    fun beforeEach() {
        setup()
    }

    private fun setup() {
        every { userService.findByNickName(USER_PARAM_VALUE) } returns AppUser(nickName = "testUser")
    }

    private fun writeValueAsString(clazz: Any): String {
        return try {
            objectMapper.writeValueAsString(clazz)
        } catch (e: Exception) {
            println("unable to parse class object")
            ""
        }
    }

    companion object {
        private const val HEADER_NAME = "nick-name"
        private const val HEADER_VALUE = "testUser"
        private const val USER_PARAM_NAME = "user"
        private const val USER_PARAM_VALUE = "testUser"
    }
}
