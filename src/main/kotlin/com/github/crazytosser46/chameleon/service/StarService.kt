package com.github.crazytosser46.chameleon.service

import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest

interface StarService {
    suspend fun getMockResponse(serverHttpRequest: ServerHttpRequest): ResponseEntity<Any>
}