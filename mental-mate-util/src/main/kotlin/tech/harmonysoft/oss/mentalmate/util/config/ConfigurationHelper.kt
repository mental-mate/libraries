package tech.harmonysoft.oss.mentalmate.util.config

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class ConfigurationHelper(
    private val context: ApplicationContext
) {

    fun verifyPropertyIsSet(propertyName: String) {
        if (context.environment.getProperty(propertyName).isNullOrBlank()) {
            throw IllegalStateException("mandatory property '$propertyName' is undefined")
        }
    }
}