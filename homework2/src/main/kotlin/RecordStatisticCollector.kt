interface RecordStatisticCollector {
    fun getRecordCount(
        hashtag: String,
        lookBackHours: Long
    ): List<Long>
}