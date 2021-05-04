package com.vision.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MessagesAppApplication

fun main(args: Array<String>) {
    runApplication<MessagesAppApplication>(*args)
}
