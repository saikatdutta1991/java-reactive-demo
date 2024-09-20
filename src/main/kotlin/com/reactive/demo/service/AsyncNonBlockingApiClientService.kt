package com.reactive.demo.service

import com.reactive.demo.dto.DataDto
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.lang.Exception

@Service
class AsyncNonBlockingApiClientService: ApiClientService() {

    @Autowired
    private lateinit var webClient: WebClient

    private var logger = LoggerFactory.getLogger(AsyncNonBlockingApiClientService::class.java)

    suspend fun getData(): List<DataDto> {
        return coroutineScope {
            numberOfRecordsIds
                .flatMap {
                    it.map { "${fetchDataBaseEndpoint}/${it}" }
                        .map { uri -> async { getData(uri) ?: throw Exception("Failed to get data for ${uri}") } }
                        .awaitAll()
                }

        }
    }

    suspend fun getData(uri: String): DataDto? {
        return webClient
            .get()
            .uri(uri)
            .retrieve()
            .bodyToMono(DataDto::class.java)
            .doOnSubscribe { logger.info("Fetching data for {}", uri) }
            .map { response ->
                logger.info("Fetched data {}", response.id)
                response
            }
            .awaitSingle()
    }
}