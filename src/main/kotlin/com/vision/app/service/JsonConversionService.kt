package com.vision.app.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class JsonConversionService(private val objectMapper: ObjectMapper) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun mapToJson(obj: Any?): String? {
        return try {
            objectMapper.writeValueAsString(obj)
        } catch (e: JsonProcessingException) {
            logger.error("Error while trying to map to JSON", e)
            null
        }
    }
}
