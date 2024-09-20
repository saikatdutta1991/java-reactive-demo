package com.reactive.demo.service

abstract class ApiClientService {
    val fetchDataBaseEndpoint = "http://localhost:3000/users"
    val numberOfRecordsIds = (1..10).map { (1..100).map { it } }
}