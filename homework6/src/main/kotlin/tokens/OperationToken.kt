package tokens

enum class OperationToken(val char: Char) : Token {
    PLUS('+'),
    MINUS('-'),
    TIMES('*'),
    DIV('/');

    companion object {
        fun valueOf(char: Char) =
                values()
                .filter { it.char == char }
                .getOrElse(0) {
                    throw IllegalArgumentException("No operation '$char'")
                }
    }
}