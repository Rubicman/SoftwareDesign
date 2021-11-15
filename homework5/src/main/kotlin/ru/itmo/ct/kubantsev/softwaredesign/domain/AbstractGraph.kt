package ru.itmo.ct.kubantsev.softwaredesign.domain

abstract class AbstractGraph(vertexCount: Int, edges: Collection<Pair<Int, Int>>) : Graph {
    override val size: Int = vertexCount

    init {
        if (!edges.all { edge ->
                edge.first in 0 until vertexCount &&
                        edge.second in 0 until vertexCount
            }) {
            throw IllegalArgumentException("All vertices must be in [0; vertexCount)")
        }
    }
}