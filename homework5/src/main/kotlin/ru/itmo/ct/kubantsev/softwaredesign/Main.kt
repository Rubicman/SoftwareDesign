package ru.itmo.ct.kubantsev.softwaredesign

import ru.itmo.ct.kubantsev.softwaredesign.domain.ListGraph
import ru.itmo.ct.kubantsev.softwaredesign.drawing.AWTDrawingApi
import ru.itmo.ct.kubantsev.softwaredesign.drawing.GraphDrawer
import ru.itmo.ct.kubantsev.softwaredesign.drawing.HTMLDrawingApi


fun main() {
    //val drawer = GraphDrawer(AWTDrawingApi())
    val drawer = GraphDrawer(HTMLDrawingApi())
    val graph = ListGraph(7, listOf(
        0 to 1,
        0 to 4,
        0 to 3,
        1 to 5
    ))
    drawer.drawGraph(graph)
}