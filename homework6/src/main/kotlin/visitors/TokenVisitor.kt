package visitors

import tokens.BracketToken
import tokens.NumberToken
import tokens.OperationToken
import tokens.Token

abstract class TokenVisitor<T> {
    fun visitAll(tokens: List<Token>): T {
        tokens.forEach { token ->
            when (token) {
                is BracketToken -> visit(token)
                is NumberToken -> visit(token)
                is OperationToken -> visit(token)
            }
        }
        return getResult()
    }

    abstract fun getResult(): T
    abstract fun visit(token: BracketToken)
    abstract fun visit(token: NumberToken)
    abstract fun visit(token: OperationToken)

    companion object {
        @JvmStatic
        protected val notReadyException
            get() = RuntimeException("Can't return result because not enough tokens were proceed")
    }
}