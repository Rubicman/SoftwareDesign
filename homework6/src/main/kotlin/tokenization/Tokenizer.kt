package tokenization

import tokenization.states.State
import tokens.Token

object Tokenizer {
    fun tokenize(text: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var state = State.start { tokens.add(it) }
        for (char in text) {
            state = state.handle(char)
        }
        state.handleEnd()
        return tokens
    }
}