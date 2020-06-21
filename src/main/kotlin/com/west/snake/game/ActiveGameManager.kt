package com.west.snake.game

import com.west.snake.events.GameActionEvent
import reactor.core.publisher.FluxProcessor
import reactor.core.publisher.FluxSink

class ActiveGameManager (val game: SnakeGame, val processor: FluxProcessor<SnakeGame, SnakeGame>){
    private val sink: FluxSink<SnakeGame> = processor.sink()

    fun advanceGame(event: GameActionEvent): Boolean {
        val gameOver = !game.takeAction(event)
        sink.next(game)
        return gameOver
    }

    fun endGame() {
        processor.dispose()
    }
}