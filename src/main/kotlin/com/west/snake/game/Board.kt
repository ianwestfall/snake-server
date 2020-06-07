package com.west.snake.game
import kotlin.random.Random

class Board (val width: Int, val height: Int) {
    val snake: Snake = placeNewSnake()
    var apple: BoardCoordinate = placeNewApple()

    private fun placeNewSnake(): Snake {
        // Returns a newly placed Snake somewhere randomly on this board
        val randomX = Random.nextInt(3, this.width - 3)
        val randomY = Random.nextInt(3, this.height - 3)
        val randomDir = getRandomSnakeDirection()

        return Snake(BoardCoordinate(randomX, randomY), 3, randomDir)
    }

    private fun placeNewApple() : BoardCoordinate {
        // Returns a BoardCoordinate that does not currently contain any Snake
        var randomX = Random.nextInt(0, this.width)
        var randomY = Random.nextInt(0, this.height)
        var apple = BoardCoordinate(randomX, randomY)

        while (apple in this.snake.body) {
            // TODO: This is gonna be slower and slower as the game goes on.
            // TODO: Change this to create an array of all open spaces, and pick a random integer from 1 to the length of that array.
            randomX = Random.nextInt(0, this.width)
            randomY = Random.nextInt(0, this.height)
            apple = BoardCoordinate(randomX, randomY)
        }

        return apple
    }

    /**
     * Move the snake in its current direction and return true if it survives, false if it dies.
     */
    fun moveSnake(): Pair<Boolean, Boolean> {
        // Is the snake about to eat an apple?
        val eatingApple = willSnakeEatTheApple()

        // Is the snake about to hit a wall or itself?
        val gonnaDie = willSnakeDie()

        if (!gonnaDie) {
            snake.move(eatingApple)

            if(eatingApple) {
                apple = placeNewApple()
            }
        }

        return Pair(!gonnaDie, eatingApple)
    }

    private fun willSnakeEatTheApple() = snake.getNextCoordinate() == apple

    private fun willSnakeDie(): Boolean {
        val snakeNextCoord = snake.getNextCoordinate()

        // Is the snake going to hit the wall?
        var hitTheWall = snakeNextCoord.x < 0 || snakeNextCoord.x >= width || snakeNextCoord.y < 0
                || snakeNextCoord.y >= height

        // Is the snake going to hit itself?
        var hitItself = snake.body.contains(snakeNextCoord)

        return hitTheWall || hitItself
    }

    fun draw() {
        for (y in this.height downTo 0) {
            var row = ""
            for (x in 0..this.width) {
                row += " " + when (BoardCoordinate(x, y)) {
                    this.snake.body.first() -> "S"
                    in this.snake.body -> "s"
                    this.apple -> "A"
                    else -> "_"
                } + " "
            }
            println(row)
        }
    }
}