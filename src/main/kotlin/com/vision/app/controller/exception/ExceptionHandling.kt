package com.vision.app.controller.exception

import com.vision.app.exceptions.UserAlreadyExists
import com.vision.app.exceptions.UserNotFound
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.RuntimeException

@RestControllerAdvice
class ExceptionHandling {

    private final val logger: Logger = LoggerFactory.getLogger(javaClass)

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFound::class)
    fun handleNotFound(exception: RuntimeException): String {
        logger.warn("requested user ${exception.message} not found")
        return "User not found"
    }

    @ResponseStatus(HttpStatus.FOUND)
    @ExceptionHandler(UserAlreadyExists::class)
    fun handleUserExists(exception: RuntimeException): String {
        logger.warn("requested user ${exception.message} already exists")
        return "User already exists"
    }
}
