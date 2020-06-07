package com.west.snake.game

import com.west.snake.events.GameActionEvent
import org.slf4j.LoggerFactory

class SnakeGame (private val id: Int, boardWidth: Int, boardHeight: Int) {
    private val board = Board(boardWidth, boardHeight)
    private var score: Int = 0
    private var turnNumber: Int = 0
    private var active: Boolean = true
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Act on the given the client action and return whether or not the game is still active.
     */
    fun takeAction(gameActionEvent: GameActionEvent): Boolean {
        logger.info("User took an action on game ${id}: ${gameActionEvent.actionDirection}")

        // Set the snake's new direction
        board.snake.setNewDirection(gameActionEvent.actionDirection)

        // Move the snake
        val (stillAlive, scored) = board.moveSnake()
        active = stillAlive
        if (scored) {
            score++
        }
        turnNumber++

        return active
    }

    fun toJson(): String{
        return """{
            |"id": ${id}, 
            |"score": ${score}, 
            |"turnNumber": ${turnNumber},
            |"gameOver": ${!active}, 
            |"board": {
                |"width": ${board.width},
                |"height": ${board.height},
                |"apple": ${board.apple.toJson()},
                |"snake": {
                    |"direction": "${board.snake.direction}",
                    |"body": [${board.snake.body.joinToString(", ") { c -> c.toJson() }}]
                |}
        |}}""".trimMargin().replace("\n", "")
    }
}