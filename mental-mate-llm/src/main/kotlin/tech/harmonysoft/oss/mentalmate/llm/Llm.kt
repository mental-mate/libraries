package tech.harmonysoft.oss.mentalmate.llm

interface Llm {

    fun ask(prompt: String): String
}