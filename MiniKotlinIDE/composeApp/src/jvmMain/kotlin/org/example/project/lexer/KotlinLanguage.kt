package org.example.project.lexer


object HardKeywords {
    val all = setOf(
        "as", "as?",
        "break",
        "class",
        "continue",
        "do",
        "else",
        "false",
        "for",
        "fun",
        "if",
        "in", "!in",
        "interface",
        "is", "!is",
        "null",
        "object",
        "package",
        "return",
        "super",
        "this",
        "throw",
        "true",
        "try",
        "typealias",
        "typeof",
        "val",
        "var",
        "when",
        "while"
    )
}


object SoftKeywords {

    val all = setOf(
        "by",
        "catch",
        "constructor",
        "delegate",
        "dynamic",
        "field",
        "file",
        "finally",
        "get",
        "import",
        "init",
        "param",
        "property",
        "receiver",
        "set",
        "setparam",
        "value",
        "where"
    )
}


object ModifierKeywords {

    val all = setOf(
        "abstract",
        "actual",
        "annotation",
        "companion",
        "const",
        "crossinline",
        "data",
        "enum",
        "expect",
        "external",
        "final",
        "infix",
        "inline",
        "inner",
        "internal",
        "lateinit",
        "noinline",
        "open",
        "operator",
        "out",
        "override",
        "private",
        "protected",
        "public",
        "reified",
        "sealed",
        "suspend",
        "tailrec",
        "vararg"
    )
}


object SpecialIdentifiers {
    val all = setOf(
        "field",
        "it"
    )
}


object KotlinLanguage {
    fun resolve(word: String): TokenType =
        when {
            word in HardKeywords.all -> TokenType.HARD_KEYWORD
            word in SoftKeywords.all -> TokenType.SOFT_KEYWORD
            word in ModifierKeywords.all -> TokenType.MODIFIER
            word in SpecialIdentifiers.all -> TokenType.IDENTIFIER
            else -> TokenType.IDENTIFIER
        }
}
