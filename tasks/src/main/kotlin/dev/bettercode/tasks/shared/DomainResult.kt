package dev.bettercode.tasks.shared

import dev.bettercode.commons.events.DomainEvent
import io.vavr.control.Either
import io.vavr.control.Either.left
import io.vavr.control.Either.right

class DomainResult private constructor(private val result: Either<Failure, Success>) {

    companion object {
        fun success(): DomainResult {
            return success(listOf())
        }

        fun success(event: DomainEvent): DomainResult {
            return success(listOf(event))
        }

        fun success(events: List<DomainEvent>): DomainResult {
            return DomainResult(right(Success(events)))
        }

        fun failure(reason: String): DomainResult {
            return DomainResult(left(Failure(reason)))
        }
    }

    val events get() = if (this.result.isRight) this.result.get().events else listOf()

    val successful get() = this.result.isRight

    val failure get() = this.result.isLeft

    val reason get() = if (this.result.isLeft) this.result.left.reason else null
}

class Failure(val reason: String)

class Success(val events: List<DomainEvent>)