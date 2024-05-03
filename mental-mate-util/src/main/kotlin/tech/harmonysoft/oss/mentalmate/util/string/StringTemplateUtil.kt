package tech.harmonysoft.oss.mentalmate.util.string

object StringTemplateUtil {

    val REGEX_VARIABLE = """\$\{(\S+)}""".toRegex()

    fun expand(template: String, variables: Map<String, Any>): String {
        val expandedPrompt = variables.entries.fold(template) { text, (key, value) ->
            val replaced = text.replace("\${$key}", value.toString())
            if (replaced == text) {
                throw IllegalArgumentException(
                    "given template doesn't have variable '$key' but it is provided for expansion. "
                    + "The variable is expected to be specified as \${$key} in the template. Given template:\n"
                    + template
                )
            }
            replaced
        }
        REGEX_VARIABLE.find(expandedPrompt)?.let { match ->
            throw IllegalArgumentException(
                "given template defines variable '${match.groupValues[1]}' but it's not provided "
                + "in the replacement rules"
            )
        }
        return expandedPrompt
    }
}