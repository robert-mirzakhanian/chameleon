package com.github.crazytosser46.chameleon.model

data class ResponseModel(
    val value: String? = null,
    val isCompressed: Boolean? = null,
    val status: Int,
    val headers: Map<String, String> = mapOf()
)