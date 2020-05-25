package com.west.snake.events

import com.west.snake.game.SnakeGame

data class GameEvent (val game: SnakeGame) {
    fun toJson(): String{
        return game.toJson()
    }
}
