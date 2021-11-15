package ru.itmo.ct.kubantsev.softwaredesign.drawing

interface DrawingApi {

    var drawingAreaWidth: Int
    var drawingAreaHeight: Int

    val screenWidth: Int
    val screenHeight: Int

    fun drawLine(start: Pair<Int, Int>, end: Pair<Int, Int>)

    fun drawCircle(center: Pair<Int, Int>, radius: Int)

    fun drawString(position: Pair<Int, Int>, string: String)

    fun showGraph()

    fun clear()
}