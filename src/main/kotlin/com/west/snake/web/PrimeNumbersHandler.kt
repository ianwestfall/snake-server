package com.west.snake.web

import com.west.snake.events.PrimeEvent
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import reactor.core.publisher.TopicProcessor

@Component
class PrimeNumbersHandler : WebSocketHandler {
    private val processor = TopicProcessor.share<PrimeEvent>("shared", 1024)

    override fun handle(session: WebSocketSession): Mono<Void> {

        return session.send(processor.map {ev -> session.textMessage("${ev.sender}:${ev.value}")})
                .and(
                    session.receive()
                        .map {webSocketMessage ->
                            val parts = webSocketMessage.payloadAsText.split(":")
                            println("Checking ${parts[0.toInt()]} for ${parts[1].toInt()}")
                            PrimeEvent(sender=parts[0].toInt(), value=parts[1].toInt())
                        }
                        .filter {ev -> isPrime(ev.value)}
                        .doOnNext {ev -> processor.onNext(ev)}
                )
    }

    fun isPrime(input: Int) = input %2 != 0
}