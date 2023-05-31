package com.github.mzr.chameleon.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver

@Configuration
class Config {

    @Bean
    fun reactivePageResolver(): HandlerMethodArgumentResolver {
        return ReactivePageableHandlerMethodArgumentResolver();
    }
}