package com.vision.app.controller.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.vision.app.domain.AppUser
import com.vision.app.domain.dto.request.UserRequestDto
import com.vision.app.repository.UserRepository
import com.vision.app.service.UserService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(UserController::class)
class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var userRepository: UserRepository

    @Test
    fun registerUserResultInOk() {

        val user = AppUser(nickName = "username")
        every { userService.save(AppUser(nickName = "username")) } returns user

        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/user/register")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(UserRequestDto(nickname = "username")))
            )
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }
}
