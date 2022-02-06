package com.github.crazytosser46.chameleon.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundMockException(override val message: String?): RuntimeException(message)