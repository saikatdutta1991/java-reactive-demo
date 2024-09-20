package com.reactive.demo.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.reactive.demo.dto.DataDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class SynchronousApiClientService: ApiClientService() {

    private var logger = LoggerFactory.getLogger(SynchronousApiClientService::class.java)

    fun getData(): List<DataDto> {
        return numberOfRecordsIds[0].map { id ->
            getData(id).also { logger.info("Fetched data {}", id) }
        }
    }

    fun getData(id: Int): DataDto {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("${fetchDataBaseEndpoint}/${id}"))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString()) // Synchronous call
        val responseBody = response.body()

        // Use Jackson's ObjectMapper to convert JSON string to DTO
        val mapper = jacksonObjectMapper()
        return mapper.readValue(responseBody, DataDto::class.java)
    }
}