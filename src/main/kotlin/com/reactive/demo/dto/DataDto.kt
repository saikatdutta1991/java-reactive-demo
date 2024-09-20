package com.reactive.demo.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class DataDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
)