package com.github.mzr.chameleon.controller

import com.github.mzr.chameleon.service.StarService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController

class StarController(
    private val starService: StarService
) {

    val log: Logger = LoggerFactory.getLogger(StarController::class.java)

    @RequestMapping("/**")
    suspend fun star(serverHttpRequest: ServerHttpRequest): ResponseEntity<Any> {
        serverHttpRequest.method
        serverHttpRequest.path
        serverHttpRequest.headers


        log.info("Get request with {} {} {}", )
        return starService.getMockResponse(serverHttpRequest)
    }
}