package com.github.crazytosser46.chameleon.controller

import com.github.crazytosser46.chameleon.service.StarService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StarController {

    @Autowired
    private lateinit var starService: StarService

    private val log = LoggerFactory.getLogger(StarController::class.java)

    @RequestMapping("/**")
    suspend fun star(serverHttpRequest: ServerHttpRequest): ResponseEntity<Any> {
        return starService.getMockResponse(serverHttpRequest)
    }
}