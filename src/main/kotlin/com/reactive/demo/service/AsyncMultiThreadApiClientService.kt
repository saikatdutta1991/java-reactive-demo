package com.reactive.demo.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.reactive.demo.dto.DataDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.Callable
import java.util.concurrent.Executors

@Service
class AsyncMultiThreadApiClientService: ApiClientService() {

    private var logger = LoggerFactory.getLogger(AsyncMultiThreadApiClientService::class.java)

    private val client = HttpClient.newHttpClient()

    fun getData(): List<DataDto> {
        val executor = Executors.newFixedThreadPool(100)

        val callables = numberOfRecordsIds
            .flatMap {
                it.map {
                    logger.info("getData({}) thread = {}", it, Thread.currentThread().name)
                    Callable {
                        getData(it).also { logger.info("Fetched data {}", it) }
                    }
                }
            }


        val futures = executor.invokeAll(callables)
        val data = futures.map { it.get() }
        executor.shutdown()
        return data
    }

    fun getData(id: Int): DataDto {
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