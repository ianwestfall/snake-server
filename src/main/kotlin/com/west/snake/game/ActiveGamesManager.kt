package com.west.snake.game

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.DirectProcessor
import reactor.core.publisher.FluxProcessor
import java.util.concurrent.ConcurrentHashMap

class NoGameException(message: String): Exception(message)

@Component
class ActiveGamesManager (@Value("\${boardWidth}") val boardWidth: Int,
                          @Value("\${boardHeight}") val boardHeight: Int) {
    var nextId: Int = 0
    val activeGames = ConcurrentHashMap<Int, SnakeGame>()
    val activeGameProcessors = ConcurrentHashMap<Int, FluxProcessor<SnakeGame, SnakeGame>>()

    fun addGame(): Int {
        // Create a new game, add it to the list of active games, create a FluxProcessor for it, and return the id
        activeGames[nextId] = SnakeGame(nextId, boardWidth, boardHeight)
        activeGameProcessors[nextId] = DirectProcessor.create<SnakeGame>().serialize()
        return nextId++
    }

    fun endGame(gameId: Int) {
        activeGames.remove(gameId)
        activeGameProcessors[gameId]?.dispose()
        activeGameProcessors.remove(gameId)
    }

    fun getGame(gameId: Int): SnakeGame {
        val activeGame = activeGames[gameId]
        if (activeGame != null) {
            return activeGame
        }
        else {
            throw NoGameException("No game with the id $gameId is active")
        }
    }

    fun getGameProcessor(gameId: Int): FluxProcessor<SnakeGame, SnakeGame> {
        val activeGameProcessor = activeGameProcessors[gameId]
        if (activeGameProcessor != null) {
            return activeGameProcessor
        }
        else {
            throw NoGameException("No game with the id $gameId is active")
        }
    }
}
