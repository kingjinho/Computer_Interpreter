package tool

import java.io.IOException
import java.io.PrintWriter
import java.util.*
import kotlin.jvm.Throws
import kotlin.system.exitProcess


@Throws(IOException::class)
fun main(args: Array<String>) {
    if (args.size != 1) {
        System.err.println("Usage: generate ast <output directory>")
        exitProcess(64)
    }
    val outputDir = args[0]
    defineAst(outputDir, "Expr", listOf("Binary   : Expr left, Token operator, Expr right",
            "Grouping : Expr expression",
            "Literal  : Object value",
            "Unary    : Token operator, Expr right"))
}

//base Expr class
@Throws(IOException::class)
private fun defineAst(outputDir: String, baseName: String, types: List<String>) {
    val path = "$outputDir/$baseName.java"

    val writer = PrintWriter(path, "UTF-8")
    writer.println("package com.craftinginterpreters.lox;")
    writer.println()
    writer.println("import java.util.List;")
    writer.println()
    writer.println("abstract class $baseName {")

    for (type in types) {
        val className = type.split(":")[0].trim()
        val fields = type.split(":")[1].trim()
        defineType(writer, baseName, className, fields)
    }

    writer.println("}")
    writer.close()

}

private fun defineType(writer: PrintWriter, baseName: String, className: String, fieldList: String) {
    writer.println(" static class $className extends $baseName {")
    //constructor
    writer.println("     $className ($fieldList) {")

    //store parameters in fields
    val fields = fieldList.split(", ")
    for (field in fields) {
        val name = field.split(" ")[1]
        writer.println("   this.$name = $name;")
    }

    writer.println("    }")

    //fields
    writer.println()
    for (field in fields) {
        writer.println("   final $field;")
    }

    writer.println("  }")
}
