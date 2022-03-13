import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class TestClock: Clock() {
    var time: ZonedDateTime = ZonedDateTime.parse("2022-03-07T10:00+03:00")

    override fun instant(): Instant = time.toInstant()

    override fun withZone(zone: ZoneId?): Clock = this

    override fun getZone(): ZoneId = time.zone
}