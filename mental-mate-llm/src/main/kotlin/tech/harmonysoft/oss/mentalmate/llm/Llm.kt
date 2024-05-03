package tech.harmonysoft.oss.mentalmate.llm

import tech.harmonysoft.oss.mentalmate.util.string.StringTemplateUtil

interface Llm {

    fun ask(prompt: String): String

    fun ask(promptTemplate: String, variables: Map<String, Any>): String {
        return ask(StringTemplateUtil.expand(promptTemplate, variables))
    }
}