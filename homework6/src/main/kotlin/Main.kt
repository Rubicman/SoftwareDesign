import tokenization.Tokenizer
import visitors.CalcVisitor
import visitors.ParseVisitor
import visitors.PrintVisitor

fun main() {
    val expression = readLine()!!
    val tokens = Tokenizer.tokenize(expression)
    val rpnTokens = ParseVisitor().visitAll(tokens)
    PrintVisitor().visitAll(rpnTokens)
    print(CalcVisitor().visitAll(rpnTokens))
}