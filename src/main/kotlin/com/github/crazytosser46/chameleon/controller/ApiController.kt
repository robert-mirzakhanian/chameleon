package com.github.crazytosser46.chameleon.controller

import com.github.crazytosser46.chameleon.model.MockModel
import com.github.crazytosser46.chameleon.model.RequestModel
import com.github.crazytosser46.chameleon.service.ApiService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ApiController {

    @Autowired
    private lateinit var apiService: ApiService

    private val log = LoggerFactory.getLogger(ApiController::class.java)

    @GetMapping("/test")
    suspend fun createRequest() {
        log.info("Got api request with uri {}", "123")
    }

    @PostMapping("/mock/create")
    suspend fun createMock(@RequestBody mockModel: MockModel): ResponseEntity<String?> {
        log.info("Got request for creating mock {}", mockModel)
        apiService.createMock(mockModel)
        log.info("Mock created successful!")
        return ResponseEntity.ok().build()
    }
}