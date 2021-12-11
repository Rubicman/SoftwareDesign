package tokenization.states

import tokens.NumberToken
import tokens.Token

data class NumberState(override val consumer: (Token) -> Unit, private val number: Int) : State() {
    override fun handleWhitespace(): State {
        handleEnd()
        return EmptyState(consumer)
    }

    override fun handleDigit(digit: Int): State = NumberState(consumer, number * 10 + digit)


    override fun handleMathSymbol(token: Token): State {
        handleEnd()
        consumer(token)
        return EmptyState(consumer)
    }

    override fun handleEnd() {
        consumer(NumberToken(number))
    }
}