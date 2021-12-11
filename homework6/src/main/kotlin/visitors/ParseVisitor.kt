package visitors

import tokens.BracketToken
import tokens.NumberToken
import tokens.OperationToken
import tokens.Token

class ParseVisitor : TokenVisitor<List<Token>>() {
    private var currentNode: Node = Node(null)
    private var priority = 0

    override fun getResult(): List<Token> {
        var node = currentNode
        while (node.parent != null) {
            node = node.parent!!
        }
        return node.getResult()
    }

    override fun visit(token: BracketToken) {
        if (currentNode.left != null || currentNode.right != null) unexpectedBracket(token)
        if (token == BracketToken.LEFT) {
            if (currentNode.token != null) unexpectedBracket(token)
            priority += PRIORITY_LEVELS
        }
        if (token == BracketToken.RIGHT) {
            if (currentNode.token == null) unexpectedBracket(token)
            priority -= PRIORITY_LEVELS
        }
    }

    override fun visit(token: NumberToken) {
        if (currentNode.token != null) {
            throw IllegalArgumentException("Meet number '${token.value}' in unexpected place")
        }
        currentNode.token = token
    }

    override fun visit(token: OperationToken) {
        if (currentNode.left != null || currentNode.right != null || currentNode.token == null) {
            throw IllegalArgumentException("Meet operation '${token.char}' in unexpected place")
        }
        val priority = priority + getPriority(token)
        while (currentNode.parent != null && currentNode.parent!!.priority >= priority) {
            currentNode = currentNode.parent!!
        }
        val node = Node(currentNode.parent, priority, currentNode, token)
        if (currentNode.parent != null) {
            if (currentNode.parent!!.left === currentNode) {
                currentNode.parent!!.left = node
            } else {
                currentNode.parent!!.right = node
            }
        }
        currentNode.parent = node
        node.right = Node(node)
        currentNode = node.right!!
    }

    private fun unexpectedBracket(token: BracketToken) {
        throw IllegalArgumentException("Meet bracket '${token.char}' in unexpected place")
    }

    private fun getPriority(token: OperationToken) = when(token) {
        OperationToken.PLUS -> 0
        OperationToken.MINUS -> 0
        OperationToken.TIMES -> 1
        OperationToken.DIV -> 1
    }

    private class Node(
        var parent: Node?,
        var priority: Int = Int.MAX_VALUE,
        var left: Node? = null,
        var token: Token? = null,
        var right: Node? = null,
    ) {
        fun getResult(): List<Token> {
            if (token == null) throw notReadyException
            if (left != null && right != null) {
                return left!!.getResult() + right!!.getResult() + token!!
            }
            if (left == null && right == null) {
                return listOf(token!!)
            }
            throw notReadyException
        }
    }

    companion object {
        private const val PRIORITY_LEVELS = 2
    }
}