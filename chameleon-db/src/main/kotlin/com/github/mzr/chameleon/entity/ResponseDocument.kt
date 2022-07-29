package com.github.mzr.chameleon.entity


data class ResponseDocument(
    val value: String,
    val isCompressed: Boolean = false,
    val status: Int,
    val headers: Map<String, String>? = null
)