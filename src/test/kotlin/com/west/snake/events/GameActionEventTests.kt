package com.west.snake.events

import com.west.snake.game.ActionDirection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameActionEventTests {
    @Test
    fun testGameActionEventFromJson() {
        var json = """{"rawActionDirection": 0}"""
        var parsedEvent = gameActionEventFromJson(json)
        assertEquals(parsedEvent.actionDirection, ActionDirection.FORWARD)

        json = """{"rawActionDirection": 1}"""
        parsedEvent = gameActionEventFromJson(json)
        assertEquals(parsedEvent.actionDirection, ActionDirection.LEFT)

        json = """{"rawActionDirection": 2}"""
        parsedEvent = gameActionEventFromJson(json)
        assertEquals(parsedEvent.actionDirection, ActionDirection.RIGHT)
    }
}