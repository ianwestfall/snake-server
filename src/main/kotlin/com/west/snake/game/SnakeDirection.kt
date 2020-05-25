package com.west.snake.game

import kotlin.random.Random

enum class SnakeDirection {
    UP {
        override fun getLeftDirection() = LEFT
        override fun getRightDirection() = RIGHT
        override fun getOpposite() = DOWN
    },
    DOWN {
        override fun getLeftDirection() = RIGHT
        override fun getRightDirection() = LEFT
        override fun getOpposite() = UP
    },
    LEFT {
        override fun getLeftDirection() = DOWN
        override fun getRightDirection() = UP
        override fun getOpposite() = RIGHT
    },
    RIGHT {
        override fun getLeftDirection() = UP
        override fun getRightDirection() = DOWN
        override fun getOpposite() = LEFT
    };

    abstract fun getLeftDirection(): SnakeDirection
    abstract fun getRightDirection(): SnakeDirection
    abstract fun getOpposite(): SnakeDirection
}

fun getRandomSnakeDirection() = when (Random.nextInt(0, 4)) {
    0 -> SnakeDirection.UP
    1 -> SnakeDirection.DOWN
    2 -> SnakeDirection.LEFT
    3 -> SnakeDirection.RIGHT
    else -> SnakeDirection.UP
}
