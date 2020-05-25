package com.west.snake

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SnakeApplication

fun main(args: Array<String>) {
	runApplication<SnakeApplication>(*args)
}