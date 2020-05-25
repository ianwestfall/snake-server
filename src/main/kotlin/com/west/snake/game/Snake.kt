package com.west.snake.game

import java.util.*

class Snake(headLocation: BoardCoordinate, length: Int, var direction: SnakeDirection) {
    val body: LinkedList<BoardCoordinate> = LinkedList()

    init {
        this.body.add(headLocation)
        while (this.body.size < length) {
            this.body.add(this.getNextCoordinateInDirection(this.body.last(), this.direction.getOpposite()))
        }
    }

    fun setNewDirection(actionDirection: ActionDirection) {
        when (actionDirection) {
            ActionDirection.RIGHT -> direction = direction.getRightDirection()
            ActionDirection.LEFT -> direction = direction.getLeftDirection()
            else -> {}
        }
    }

    fun move(grow: Boolean = false) {
        body.push(getNextCoordinate())
        if (!grow) {
            body.removeLast()
        }
    }

    fun getNextCoordinate(): BoardCoordinate {
        return getNextCoordinateInDirection(body[0])
    }

    private fun getNextCoordinateInDirection(start: BoardCoordinate, direction: SnakeDirection = this.direction) =
            when (direction) {
                SnakeDirection.UP -> BoardCoordinate(start.x, start.y + 1)
                SnakeDirection.DOWN -> BoardCoordinate(start.x, start.y - 1)
                SnakeDirection.LEFT -> BoardCoordinate(start.x - 1, start.y)
                SnakeDirection.RIGHT -> BoardCoordinate(start.x + 1, start.y)
            }
}
