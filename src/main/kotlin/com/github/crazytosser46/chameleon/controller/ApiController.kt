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
class ApiController(
    private val apiService: ApiService
) {

    private val log = LoggerFactory.getLogger(ApiController::class.java)

    @PostMapping("/mock/create")
    suspend fun createMock(@RequestBody mockModel: MockModel): ResponseEntity<String?> {
        log.info("Got request for creating mock {}", mockModel)
        val mockDocument = apiService.createMock(mockModel)
        log.info("Mock created successful with id ${mockDocument.id}!")
        return ResponseEntity.ok("Mock created successful! Id ${mockDocument.id}")
    }

    @GetMapping("/mock/all")
    suspend fun getAllMocks() {
        log.info("Got request for all mocks {}")
        apiService.getAllMocks()
    }
}