package com.github.mzr.chameleon.controller

import com.github.mzr.chameleon.model.MockDto
import com.github.mzr.chameleon.service.ApiService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ApiController(
    private val apiService: ApiService
) {

    private val log = LoggerFactory.getLogger(ApiController::class.java)

    @PostMapping("/mock/create")
    suspend fun createMock(@RequestBody mockDto: MockDto): String {
        log.info("Got request for creating mock {}", mockDto)
        val mockDocument = apiService.createMock(mockDto)
        log.info("Mock created successful with id ${mockDocument.id}!")
        return "Mock created successful! Id ${mockDocument.id}"
    }

    @GetMapping("/mock/all")
    suspend fun getAllMocks(): List<MockDto> {
        log.info("Got request for all mocks")
        val mocks = apiService.getAllMocks()
        log.info("Return mock site ${mocks.size}")
        return mocks
    }
}