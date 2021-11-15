package ru.itmo.ct.kubantsev.softwaredesign.domain

class ListGraph(
    vertexCount: Int,
    edges: Collection<Pair<Int, Int>>
) : AbstractGraph(vertexCount, edges) {
    private val edgeList: List<Set<Int>>

    override fun getOutcomeEdges(vertex: Int): Set<Int> =
        edgeList[vertex]

    init {
        val edgeMap: Map<Int, Set<Int>> = edges
            .groupBy { pair -> pair.first }
            .mapValues { entry ->
                entry
                    .value
                    .map { pair -> pair.second }
                    .toSet()
            }
        edgeList = ArrayList((0 until vertexCount).map { index -> edgeMap[index] ?: emptySet() })
    }
}
