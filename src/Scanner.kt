import java.util.ArrayList
import TokenType.*


class Scanner(val source: String) {
    private var tokens = ArrayList<Token>()
    private var start = 0
    private var current = 0
    private var line = 1

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }
        tokens.add(Token(EOF, "", null, line))
        return tokens
    }

    private fun isAtEnd(): Boolean {
        return current >= source.length
    }

    //Recognizing Lexemes
    private fun scanToken() {
        var c = advance()
        when (c) { //single character lexeme
            '(' -> addToken(LEFT_PAREN)
            ')' -> addToken(RIGHT_PAREN)
            '{' -> addToken(LEFT_BRACE)
            '}' -> addToken(RIGHT_BRACE)
            ',' -> addToken(COMMA)
            '.' -> addToken(DOT)
            '-' -> addToken(MINUS)
            '+' -> addToken(PLUS)
            ';' -> addToken(SEMICOLON)
            '*' -> addToken(STAR)
            '!' -> addToken(match('=') then BANG_EQUAL ?: BANG)
            '=' -> addToken(match('=') then EQUAL_EQUAL ?: EQUAL)
            '<' -> addToken(match('=') then LESS_EQUAL ?: LESS)
            '>' -> addToken(match('=') then GREATER_EQUAL ?: GREATER)
            '/' -> if (match('/')) {
                while (peek() != '\n' && !isAtEnd()) advance()
            } else
                addToken(SLASH)

            else -> Lox.error(line, "Unexpected character")
        }
    }

    private fun advance(): Char {
        current++
        return source.elementAt(current - 1)
    }

    private fun addToken(type: TokenType) {
        addToken(type, null)
    }

    private fun addToken(type: TokenType, literal: Object?) {
        val text = source.substring(start, current)
        tokens.add(Token(type, text, literal, line))
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source.elementAt(current) != expected) return false

        current++
        return true
    }

    private fun peek(): Char {
        if(isAtEnd()) return "\\0" as Char // \0
        return source.elementAt(current)
    }

}