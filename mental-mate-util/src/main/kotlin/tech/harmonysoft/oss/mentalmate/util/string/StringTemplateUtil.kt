package tech.harmonysoft.oss.mentalmate.util.string

import java.util.concurrent.ConcurrentHashMap

object StringTemplateUtil {

    private val REGEXES = ConcurrentHashMap<Pair<String, String>, Regex>()
    private val CHARACTERS_TO_ESCAPE = listOf("\\", "[", "]", "(", ")", "$").map {
        it to "\\$it"
    }

    fun expand(template: String, variables: Map<String, Any>, prefix: String = "$[", suffix: String = "]$"): String {
        val expandedPrompt = variables.entries.fold(template) { text, (key, value) ->
            val replaced = text.replace("$prefix$key$suffix", value.toString())
            if (replaced == text) {
                throw IllegalArgumentException(
                    "given template doesn't have variable '$key' but it is provided for expansion. "
                    + "The variable is expected to be specified as $prefix$key$suffix in the template. Given template:\n"
                    + template
                )
            }
            replaced
        }
        val regex = REGEXES.computeIfAbsent(prefix to suffix) {
            Regex(escapeRegex(prefix) + "(\\S+)" + escapeRegex(suffix))
        }
        regex.find(expandedPrompt)?.let { match ->
            throw IllegalArgumentException(
                "given template defines variable '${match.groupValues[1]}' but it's not provided "
                + "in the replacement rules"
            )
        }
        return expandedPrompt
    }

    fun escapeRegex(s: String): String {
        return CHARACTERS_TO_ESCAPE.fold(s) { result, (from, to) ->
            result.replace(from, to)
        }
    }
}