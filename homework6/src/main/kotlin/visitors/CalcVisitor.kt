package visitors

import tokens.BracketToken
import tokens.NumberToken
import tokens.OperationToken
import java.util.*

class CalcVisitor: TokenVisitor<Int>() {
    private val stack = Stack<Int>()
    override fun getResult(): Int {
        if (stack.empty()) throw notReadyException
        return stack.peek()
    }

    override fun visit(token: BracketToken) {
        throw IllegalArgumentException("Meet bracket '${token.char}' in unexpected place")
    }

    override fun visit(token: NumberToken) {
        stack.add(token.value)
    }

    override fun visit(token: OperationToken) {
        val b = stack.pop()
        val a = stack.pop()
        stack.add(when (token) {
            OperationToken.PLUS -> a + b
            OperationToken.MINUS -> a - b
            OperationToken.TIMES -> a * b
            OperationToken.DIV -> a / b
        })
    }
}