package com.github.mzr.chameleon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class ChameleonApplicationMock

fun main(args: Array<String>) {
    runApplication<ChameleonApplicationMock>(*args)
}