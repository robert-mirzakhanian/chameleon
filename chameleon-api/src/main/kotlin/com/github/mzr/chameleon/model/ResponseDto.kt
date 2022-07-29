package com.github.mzr.chameleon.model

data class ResponseDto(
    val value: String? = null,
    val isCompressed: Boolean? = null,
    val status: Int,
    val headers: Map<String, String> = mapOf()
)