import java.util.ArrayList
import TokenType.*


class Scanner(val source: String) {
    private var tokens = ArrayList<Token>()
    private var start = 0
    private var current = 0
    private var line = 1


    companion object {
        val keywords = mutableMapOf("and" to AND,
                "class" to CLASS,
                "else" to ELSE,
                "false" to FALSE,
                "for" to FOR,
                "fun" to FUN,
                "if" to IF,
                "nil" to NIL,
                "or" to OR,
                "print" to PRINT,
                "return" to RETURN,
                "super" to SUPER,
                "this" to THIS,
                "true" to TRUE,
                "var" to VAR,
                "while" to WHILE
        )
    }

    private fun addKeyWords() {

    }

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
        when (val c = advance()) { //single character lexeme
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
            } else {
                addToken(SLASH)
            }
            '\n' -> line++
            ' ', '\r', '\t' -> {
                null
            }

            '"' -> string()
            else -> when {
                isDigit(c) -> number()
                isAlpha(c) -> identifier()
                else -> error(line, "Unexpected character")
            }
        }
    }

    private fun advance(): Char {
        current++
        return source.elementAt(current - 1)
    }

    private fun addToken(type: TokenType) {
        addToken(type, null)
    }

    private fun addToken(type: TokenType, literal: Any?) {
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
        if (isAtEnd()) return 0.toChar()
        return source.elementAt(current)
    }

    private fun string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++ //allowing multiline string
            advance()
        }
        if (isAtEnd()) {
            error(line, "Unterminated string.")
            return
        }

        advance()

        //trim the surrounding quotes
        val value: String = source.substring(start + 1, current - 1)
        addToken(STRING, value as Object)
    }

    private fun isDigit(c: Char): Boolean {
        return c in '0'..'9'
    }

    private fun number() {
        while (isDigit(peek())) advance()
        if (peek() == '.' && isDigit(peekNext())) {
            advance() // comsume "."

            while (isDigit(peek())) advance()
        }

        addToken(NUMBER, source.substring(start, current).toDouble() as Object)
    }

    private fun peekNext(): Char {
        if (current + 1 >= source.length) return "\\0" as Char
        return source.elementAt(current + 1)
    }

    private fun identifier() {
        while (isAlphaNumeric(peek())) advance()

        val text = source.substring(start, current)
        var tokenType = keywords[text]
        if (tokenType == null) {
            tokenType = IDENTIFIER
        }
        addToken(IDENTIFIER)
    }

    private fun isAlpha(c: Char): Boolean {
        return c in 'a'..'z' || c in 'A'..'Z' || c == '_'
    }

    private fun isAlphaNumeric(c: Char): Boolean {
        return isAlpha(c) || isDigit(c)
    }

}