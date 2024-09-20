package com.reactive.demo.dto

data class GetDataResponseDto(
    val durationNanoseconds: Long,
    val durationSeconds: Double,
    val data: List<DataDto>
)