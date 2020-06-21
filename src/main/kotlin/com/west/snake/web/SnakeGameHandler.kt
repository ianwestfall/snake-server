package com.west.snake.web

import com.west.snake.events.GameEvent
import com.west.snake.events.gameActionEventFromJson
import com.west.snake.game.ActiveGamesManager
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class SnakeGameHandler (val activeGamesManager: ActiveGamesManager): WebSocketHandler{
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handle(session: WebSocketSession): Mono<Void> {
        val gameId = activeGamesManager.addGame()

        logger.info("$gameId - New session")

        val input = session.receive().map{webSocketMessage ->
            logger.info("$gameId - new message: ${webSocketMessage.payloadAsText}")

            // Convert the websocket message to a GameActionEvent
            gameActionEventFromJson(webSocketMessage.payloadAsText)
        }.doOnNext { gameActionEvent ->
            logger.info("$gameId - taking action")

            // Process the game action
            activeGamesManager.advanceGame(gameId, gameActionEvent)
        }.then()

        val output = session.send(
                // Send the initial game state to the client
                Mono.just(session.textMessage(GameEvent(activeGamesManager.getGame(gameId)).toJson())))

                // Send each updated game state as events are processed
                .and(session.send(activeGamesManager.getGameProcessor(gameId).map {updatedGame ->
                    logger.info("$gameId - responding with new game status")

                    // Send the newly updated game state back to the client
                    session.textMessage(GameEvent(updatedGame).toJson())
                }))

        return Mono.zip(input, output).then()
    }
}