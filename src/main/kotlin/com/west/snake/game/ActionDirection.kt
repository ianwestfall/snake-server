package com.west.snake.game

enum class ActionDirection(val direction: Int) {
    FORWARD(0),
    LEFT(1),
    RIGHT(2);

    companion object {
        fun fromValue(directionValue: Int) = when (directionValue) {
            0 -> ActionDirection.FORWARD
            1 -> ActionDirection.LEFT
            2 -> ActionDirection.RIGHT
            else -> throw IllegalArgumentException("$directionValue is not a valid ActionDirection value")
        }
    }
}

