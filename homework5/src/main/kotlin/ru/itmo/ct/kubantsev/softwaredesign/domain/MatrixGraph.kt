package ru.itmo.ct.kubantsev.softwaredesign.domain

class MatrixGraph(
    vertexCount: Int,
    edges: Collection<Pair<Int, Int>>
) : AbstractGraph(vertexCount, edges) {
    private val adjacencyMatrix = Array(vertexCount) { Array(vertexCount) { false } }

    init {
        edges.forEach { edge ->
            adjacencyMatrix[edge.first][edge.second] = true
        }
    }

    override fun getOutcomeEdges(vertex: Int): Set<Int> =
        (0 until size).filter { to -> adjacencyMatrix[vertex][to] }.toSet()
}