package tech.harmonysoft.oss.mentalmate.util.string

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class StringTemplateUtilTest {

    @Test
    fun `when template with valid variables is provided then it is correctly expanded`() {
        val expanded = StringTemplateUtil.expand(
            "\${object} is \${subject}",
            mapOf(
                "object" to "a dog",
                "subject" to "an animal"
            )
        )
        Assertions.assertThat(expanded).isEqualTo("a dog is an animal")
    }

    @Test
    fun `when more variables than necessary are provided then template expansion fails`() {
        assertThrows<IllegalArgumentException> {
            StringTemplateUtil.expand("\${object} is \${subject}", mapOf(
                "object" to "a dog",
                "subject" to "an animal",
                "key" to "value"
            ))
        }
    }

    @Test
    fun `when less variables than necessary are provided then template expansion fails`() {
        assertThrows<IllegalArgumentException> {
            StringTemplateUtil.expand("\${object} is \${subject}", mapOf("object" to "a dog"))
        }
    }
}