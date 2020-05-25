package com.west.snake.events

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.west.snake.game.ActionDirection

data class GameActionEvent (val rawActionDirection: Int) {
    val actionDirection = ActionDirection.fromValue(rawActionDirection)
}

fun gameActionEventFromJson(eventText: String): GameActionEvent {
    val mapper = jacksonObjectMapper()
    return mapper.readValue(eventText)
}
