package com.west.snake.web

import com.west.snake.events.GameEvent
import com.west.snake.game.ActiveGamesManager
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class MonitoringConnectionAlreadyInitializedException(message: String): Exception(message)

@Component
class SnakeGameMonitorHandler (val activeGamesManager: ActiveGamesManager): WebSocketHandler {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handle(session: WebSocketSession): Mono<Void> {
        logger.info("New monitoring session")
        var initialized = false

        val input = session.receive().map{_ ->
            if (initialized) {
                throw MonitoringConnectionAlreadyInitializedException("Monitoring connection already initialized")
            }

            logger.info("New connecion request")
            initialized = true
        }.then()

        val output = session.send(activeGamesManager.monitoringProcessor.map {updatedGame ->
            logger.info("Sending monitoring info for an event from game ${updatedGame.id}")

            session.textMessage(GameEvent(updatedGame).toJson())
        })

        return Mono.zip(input, output).then()
    }
}