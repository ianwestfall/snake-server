@file:Suppress("unused")

package com.west.snake.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

@EnableWebFlux
@SpringBootApplication
class WebSocketConfig(val primeNumbersHandler: PrimeNumbersHandler, val snakeGameHandler: SnakeGameHandler) {
    @Bean
    fun websocketHandlerAdapter() = WebSocketHandlerAdapter()

    @Bean
    fun handlerMapping(): HandlerMapping {
        val handlerMapping = SimpleUrlHandlerMapping()
        handlerMapping.urlMap = mapOf(
                "/ws/primes" to primeNumbersHandler,
                "ws/snake/play" to snakeGameHandler
                // "ws/snake/monitor" to primeNumbersHandler
        )
        handlerMapping.order = 1
        return handlerMapping
    }
}
