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
        val game = activeGamesManager.getGame(gameId)
        val source = activeGamesManager.getGameProcessor(gameId)
        val sink = source.sink()

        logger.info("$gameId - New session")

        val input = session.receive().map{webSocketMessage ->
            logger.info("$gameId - new message: ${webSocketMessage.payloadAsText}")

            // Convert the websocket message to a GameActionEvent
            gameActionEventFromJson(webSocketMessage.payloadAsText)
        }.doOnNext { gameActionEvent ->
            logger.info("$gameId - taking action")

            // Act on the event's action and send the game to the sink
            val gameOver = !game.takeAction(gameActionEvent)
            sink.next(game)

            // Close the conection if the game has ended
            if (gameOver) {
                logger.info("$gameId - Game over")
                activeGamesManager.endGame(gameId)
            }
        }.then()

        // Send the initial game state to the client then start responding to their actions
        val output = session.send(Mono.just(session.textMessage(GameEvent(game).toJson()))).and(
                session.send(source.map {updatedGame ->
                    logger.info("$gameId - responding with new game status")

                    // Send the newly updated game state to back to the client
                    session.textMessage(GameEvent(updatedGame).toJson())
                }))

        return Mono.zip(input, output).then()
    }
}