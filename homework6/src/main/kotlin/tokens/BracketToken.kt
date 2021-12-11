package tokens

enum class BracketToken(val char: Char) : Token {
    LEFT('('),
    RIGHT(')');

    companion object {
        fun valueOf(char: Char) =
            values().filter { it.char == char }.getOrElse(0) { throw IllegalArgumentException("No bracket '$char'") }
    }
}