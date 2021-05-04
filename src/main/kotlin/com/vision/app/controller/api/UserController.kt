package com.vision.app.controller.api

import com.vision.app.domain.AppUser
import com.vision.app.domain.dto.request.UserRequestDto
import com.vision.app.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/register", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun registerUser(@RequestBody userRequestDto: UserRequestDto) {
        userService.save(AppUser(nickName = userRequestDto.nickname))
    }
}
