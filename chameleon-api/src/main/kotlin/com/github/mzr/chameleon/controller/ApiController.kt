package com.github.mzr.chameleon.controller

import com.github.mzr.chameleon.model.MockDto
import com.github.mzr.chameleon.service.ApiService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
@Tag(name = "General methods")
class ApiController(
    private val apiService: ApiService
) {

    private val log = LoggerFactory.getLogger(ApiController::class.java)

    @Operation(description = "Create mock")
    @ApiResponse(description = "Response documentId", responseCode = "200")
    @PostMapping("/mock/create")
    suspend fun createMock(@RequestBody mockDto: MockDto): String {
        log.info("Got request for creating mock {}", mockDto)
        val mockDocument = apiService.createMock(mockDto)
        log.info("Mock created successful with id ${mockDocument.id}!")
        return mockDocument.id!!
    }

    @Operation(description = "All mocks by pageable parameters")
    @ApiResponse(
        description = "Response all document", responseCode = "200",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = MockDto::class))
            )
        ]
    )
    @GetMapping("/mock/{id}")
    suspend fun getMockById(@PathVariable id: String): MockDto {
        log.info("Got request for all mocks")
        val mock = apiService.getMockById(id)
        log.info("Return mock site $mock")
        return mock
    }

    @Operation(description = "All mocks by pageable parameters")
    @ApiResponse(
        description = "Response all document", responseCode = "200",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = MockDto::class))
            )
        ]
    )
    @GetMapping("/mock/all")
    suspend fun getAllMocks(pageable: Pageable): List<MockDto> {
        log.info("Got request for all mocks")
        val mocks = apiService.getAllMocks(pageable)
        log.info("Return mock site ${mocks.size}")
        return mocks
    }
}