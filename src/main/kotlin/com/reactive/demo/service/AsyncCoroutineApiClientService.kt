package com.reactive.demo.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.reactive.demo.dto.DataDto
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class AsyncCoroutineApiClientService: ApiClientService() {

    private var logger = LoggerFactory.getLogger(AsyncCoroutineApiClientService::class.java)

    suspend fun getData(): List<DataDto> {
        return withContext(Dispatchers.IO) {
            numberOfRecordsIds
                .flatMap {
                    it.map {
                        async {
                            logger.info("getData({}) thread = {}", it, Thread.currentThread().name)
                            getData(it).also { logger.info("Fetched data {}", it) }
                        }
                    }.awaitAll()
                }

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
        logger.info("getData({}) thread = {}", id, Thread.currentThread().name)
        return mapper.readValue(responseBody, DataDto::class.java)
    }
}