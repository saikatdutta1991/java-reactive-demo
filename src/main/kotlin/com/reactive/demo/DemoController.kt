package com.reactive.demo

import com.reactive.demo.dto.GetDataResponseDto
import com.reactive.demo.dto.PingResponseDto
import com.reactive.demo.service.AsyncCoroutineApiClientService
import com.reactive.demo.service.AsyncMultiThreadApiClientService
import com.reactive.demo.service.AsyncNonBlockingApiClientService
import com.reactive.demo.service.SynchronousApiClientService
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoController {

    @Autowired
    private lateinit var synchronousApiClientService: SynchronousApiClientService

    @Autowired
    private lateinit var asyncNonBlockingApiClientService: AsyncNonBlockingApiClientService

    @Autowired
    private lateinit var asyncCoroutineApiClientService: AsyncCoroutineApiClientService

    @Autowired
    private lateinit var asyncMultiThreadApiClientService: AsyncMultiThreadApiClientService

    @GetMapping("/ping")
    fun ping(): PingResponseDto {
        return PingResponseDto(success = true)
    }

    @GetMapping("/data/sync")
    fun getDataSync(): GetDataResponseDto {
        val start = System.nanoTime()
        val data = synchronousApiClientService.getData()
        val end = System.nanoTime()

        return GetDataResponseDto(
            durationNanoseconds = end - start,
            durationSeconds = (end - start) / 1_000_000_000.0,
            data = data
        )
    }

    @GetMapping("/data/async-non-blocking")
    fun getDataAsyncNonBlocking(): GetDataResponseDto {
        val start = System.nanoTime()
        val data = runBlocking { asyncNonBlockingApiClientService.getData() }
        val end = System.nanoTime()

        return GetDataResponseDto(
            durationNanoseconds = end - start,
            durationSeconds = (end - start) / 1_000_000_000.0,
            data = data
        )
    }

    @GetMapping("/data/async-coroutine")
    fun getDataAsyncCoroutine(): GetDataResponseDto {
        val start = System.nanoTime()
        val data = runBlocking { asyncCoroutineApiClientService.getData() }
        val end = System.nanoTime()

        return GetDataResponseDto(
            durationNanoseconds = end - start,
            durationSeconds = (end - start) / 1_000_000_000.0,
            data = data
        )
    }

    @GetMapping("/data/async-multi-thread")
    fun getDataAsyncMultiThread(): GetDataResponseDto {
        val start = System.nanoTime()
        val data = runBlocking { asyncMultiThreadApiClientService.getData() }
        val end = System.nanoTime()

        return GetDataResponseDto(
            durationNanoseconds = end - start,
            durationSeconds = (end - start) / 1_000_000_000.0,
            data = data
        )
    }
}