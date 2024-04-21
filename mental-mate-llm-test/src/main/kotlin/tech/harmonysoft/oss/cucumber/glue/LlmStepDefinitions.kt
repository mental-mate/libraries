package tech.harmonysoft.oss.cucumber.glue

import io.cucumber.java.en.Given
import org.springframework.beans.factory.annotation.Autowired
import tech.harmonysoft.oss.mentalmate.llm.LlmTest

class LlmStepDefinitions {

    @Autowired private lateinit var llm: LlmTest

    @Given("^LLM replies by the following on the next request:$")
    fun configureReply(reply: String) {
        llm.setReply(reply)
    }
}