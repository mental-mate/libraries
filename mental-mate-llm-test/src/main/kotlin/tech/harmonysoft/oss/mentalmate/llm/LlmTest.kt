package tech.harmonysoft.oss.mentalmate.llm

import java.util.concurrent.atomic.AtomicReference
import org.springframework.stereotype.Component
import tech.harmonysoft.oss.test.TestAware
import tech.harmonysoft.oss.test.util.TestUtil.fail

@Component
class LlmTest : Llm, TestAware {

    private val replyRef = AtomicReference<String?>()

    override fun ask(prompt: String): String {
        return replyRef.get() ?: fail(
            "no LLM reply is configured for the prompt below:\n$prompt"
        )
    }

    fun setReply(reply: String) {
        replyRef.set(reply)
    }

    override fun onTestEnd() {
        replyRef.set(null)
    }
}