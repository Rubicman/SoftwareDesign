package tokenization.states

import tokens.Token

data class EmptyState(override val consumer: (Token) -> Unit) : State() {
    override fun handleWhitespace(): State = this

    override fun handleDigit(digit: Int): State = NumberState(consumer, digit)

    override fun handleMathSymbol(token: Token): State {
        consumer(token)
        return this
    }

    override fun handleEnd() {}
}