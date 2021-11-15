package ru.itmo.ct.kubantsev.softwaredesign.drawing

import ru.itmo.ct.kubantsev.softwaredesign.domain.Graph
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

class GraphDrawer(
    private val drawingApi: DrawingApi
) {

    companion object {
        const val INNER_WIDTH_MIN = -1.3
        const val INNER_WIDTH_MAX = 1.3

        const val RADIUS = 0.1
    }

    private var width: Int = 0

    fun drawGraph(graph: Graph) {
        width = setWindowSize()
        val positions = drawVertices(graph.size)
        drawEdges(graph, positions)
        drawingApi.showGraph()
    }

    private fun setWindowSize(): Int {
        val width = (min(drawingApi.screenWidth, drawingApi.screenHeight) * 0.9).toInt()
        drawingApi.drawingAreaWidth = width
        drawingApi.drawingAreaHeight = width
        return width
    }

    private fun drawVertices(vertexCount: Int): List<Pair<Double, Double>> {
        return (0 until vertexCount)
            .map { vertex -> vertex * 2 * Math.PI / vertexCount }
            .map { value -> cos(value) to sin(value) }
            .onEachIndexed { index, position ->
                val pixelPosition = innerPositionToPixel(position, width)
                drawingApi.drawCircle(pixelPosition, (width * RADIUS / (INNER_WIDTH_MAX - INNER_WIDTH_MIN)).toInt())
                drawingApi.drawString(pixelPosition, index.toString())
            }
    }

    private fun drawEdges(graph: Graph, positions: List<Pair<Double, Double>>) {
        for (v in 0 until graph.size) {
            for (u in graph.getOutcomeEdges(v)) {
                if (v == u) continue
                var begin = positions[v]
                var end = positions[u]
                var distance = getDistance(begin, end)
                var proportion = 1 - RADIUS / distance
                begin = ((begin.first - end.first) * proportion + end.first) to
                        ((begin.second - end.second) * proportion + end.second)
                distance -= RADIUS
                proportion = 1 - RADIUS / distance
                end = ((end.first - begin.first) * proportion + begin.first) to
                        ((end.second - begin.second) * proportion + begin.second)
                drawingApi.drawLine(
                    innerPositionToPixel(begin, width),
                    innerPositionToPixel(end, width)
                )
            }
        }
    }

    private fun getDistance(a: Pair<Double, Double>, b: Pair<Double, Double>) =
        sqrt((a.first - b.first) * (a.first - b.first) + (a.second - b.second) * (a.second - b.second))

    private fun innerPositionToPixel(value: Double, width: Int): Int {
        return ((value - INNER_WIDTH_MIN) / (INNER_WIDTH_MAX - INNER_WIDTH_MIN) * width).toInt()
    }

    private fun innerPositionToPixel(position: Pair<Double, Double>, width: Int): Pair<Int, Int> {
        return innerPositionToPixel(position.first, width) to width - innerPositionToPixel(position.second, width)
    }
}