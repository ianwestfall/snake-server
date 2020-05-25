package com.west.snake.game

data class BoardCoordinate(val x: Int, val y: Int) {
    fun toJson(): String {
        return """{"x": $x, "y": $y}"""
    }
}
