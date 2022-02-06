package com.github.crazytosser46.chameleon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class ChameleonApplication

fun main(args: Array<String>) {
    runApplication<ChameleonApplication>(*args)
}