package visitors

import tokens.BracketToken
import tokens.NumberToken
import tokens.OperationToken

class PrintVisitor: TokenVisitor<Unit>() {
    override fun getResult() {
        println()
    }

    override fun visit(token: BracketToken) {
        throw IllegalArgumentException("Bracket in reverse polish notation")
    }

    override fun visit(token: NumberToken) {
        print("${token.value} ")
    }

    override fun visit(token: OperationToken) {
        print("${token.char} ")
    }
}