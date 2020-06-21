package com.west.snake.game

import com.west.snake.events.GameActionEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.DirectProcessor
import reactor.core.publisher.FluxProcessor
import java.util.concurrent.ConcurrentHashMap

class NoGameException(message: String): Exception(message)

@Component
class ActiveGamesManager (@Value("\${boardWidth}") val boardWidth: Int,
                          @Value("\${boardHeight}") val boardHeight: Int) {
    private var nextId: Int = 0
    private val managers = ConcurrentHashMap<Int, ActiveGameManager>()
    final val monitoringProcessor = DirectProcessor.create<SnakeGame>().serialize()
    val monitoringSink = monitoringProcessor.sink()

    fun addGame(): Int {
        // Create a new game, add it to the list of active games, create a FluxProcessor for it, and return the id
        managers[nextId] = ActiveGameManager(SnakeGame(nextId, boardWidth, boardHeight),
                DirectProcessor.create<SnakeGame>().serialize())

        return nextId++
    }

    fun advanceGame(gameId: Int, event: GameActionEvent) {
        // Act on the event's action and send the game to the sink
        val manager = managers[gameId]

        if (manager != null) {
            val gameOver = manager.advanceGame(event)

            // Notify the manager processor of the new game state as well
            monitoringSink.next(manager.game)

            // Close the conection if the game has ended
            if (gameOver) {
                endGame(gameId)
            }
        } else {
            throw NoGameException("No game with the id $gameId is active")
        }

    }

    fun endGame(gameId: Int) {
        managers[gameId]?.endGame()
        managers.remove(gameId)
    }

    fun getGame(gameId: Int): SnakeGame {
        val manager = managers[gameId]
        if (manager != null) {
            return manager.game
        } else {
            throw NoGameException("No game with the id $gameId is active")
        }
    }

    fun getGameProcessor(gameId: Int): FluxProcessor<SnakeGame, SnakeGame> {
        val manager = managers[gameId]
        if (manager != null) {
            return manager.processor
        } else {
            throw NoGameException("No game with the id $gameId is active")
        }
    }
}
