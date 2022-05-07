package dev.bettercode

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@Import(MainConfig::class)
@SpringBootApplication
class OverEngineeredTodoApplication

fun main(args: Array<String>) {
    runApplication<OverEngineeredTodoApplication>(*args)
}
