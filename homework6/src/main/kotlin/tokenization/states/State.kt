package tokenization.states

import tokens.BracketToken
import tokens.OperationToken
import tokens.Token

sealed class State {

    companion object {
        fun start(consumer: (Token) -> Unit): State = EmptyState(consumer)
        val bracketChars = BracketToken.values().map { it.char }.toSet()
        val operationChars = OperationToken.values().map { it.char }.toSet()
    }

    abstract val consumer: (Token) -> Unit
    fun handle(char: Char): State =
        when {
            char.isWhitespace() -> handleWhitespace()
            char.isDigit() -> handleDigit(char.code - '0'.code)
            char in bracketChars -> handleMathSymbol(BracketToken.valueOf(char))
            char in operationChars -> handleMathSymbol(OperationToken.valueOf(char))
            else -> throw IllegalArgumentException("Unexpected char '$char'")
        }

    abstract fun handleEnd()

    protected abstract fun handleWhitespace(): State
    protected abstract fun handleDigit(digit: Int): State
    protected abstract fun handleMathSymbol(token: Token): State
}