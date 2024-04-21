package tech.harmonysoft.oss.mentalmate.util.time

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.TimeZone
import org.springframework.stereotype.Component
import tech.harmonysoft.oss.common.time.clock.ClockProvider
import tech.harmonysoft.oss.common.time.util.DateTimeHelper

@Component
class TimeHelper(
    private val clockProvider: ClockProvider,
    private val helper: DateTimeHelper
) {

    fun getTimestamp(): String {
        val clock = clockProvider.data
        val dateTime = helper.getFormatter(PATTERN).format(LocalDateTime.now(clock))
        val zone = TimeZone.getTimeZone(ZoneId.systemDefault()).getDisplayName(false, TimeZone.SHORT)
        return "$dateTime-$zone"
    }

    companion object {
        private const val PATTERN = "yyyy-MM-dd-HHmmssSSS"
    }
}