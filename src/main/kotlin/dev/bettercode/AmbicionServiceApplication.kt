package dev.bettercode

import dev.bettercode.tasks.TasksConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@Import(TasksConfiguration::class)
@SpringBootApplication
class AmbicionServiceApplication

fun main(args: Array<String>) {
	runApplication<AmbicionServiceApplication>(*args)
}
