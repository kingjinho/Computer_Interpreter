import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.system.exitProcess

class Lox {
    companion object {
        var hadError = false
        @Throws(IOException::class)
        fun main(args: Array<String>) {
            /**
             * 2 ways to run code
             * - 1:  from command line and give it a path to a file, jlox reads the file and executes it
             * - 2:  without arguments -> let you enter and execute code one line at a time , a.k.a REPL
             * */
            when {
                args.size > 1 -> {
                    println("Usage : jlox [script]")
                    exitProcess(64) //System.exit(64)
                }
                args.size == 1 ->
                    //way 1
                    runFile(args[0])
                else ->
                    //way 2
                    runPrompt()
            }
        }

        //path
        @Throws(IOException::class)
        private fun runFile(path: String) {
            val bytes = Files.readAllBytes(Paths.get(path))
            run(String(bytes, Charset.defaultCharset()))
            if(hadError) exitProcess(65) //indicate an error in the exit code
        }

        //without arguments
        @Throws(IOException::class)
        private fun runPrompt() {
            val input = InputStreamReader(System.`in`)
            val reader = BufferedReader(input)

            while (true) {
                println("> ")
                val line = reader.readLine()
                        ?: break //read a line of input from the user on the command line and returns the result
                run(line)
                hadError = false
            }
        }

        //actual core function
        private fun run(source: String) {
            val scanner = Scanner(source)
            val tokens : List<Token> = scanner.scanTokens()

            for (token in tokens) {
                println(token)
            }
        }

        fun error(line: Int, message: String) {
            report(line, "", message);
        }

        private fun report(line: Int, where: String, message: String) {
            System.err.println("[line $line] Error $where: $message")
            hadError = true
        }

    }
}