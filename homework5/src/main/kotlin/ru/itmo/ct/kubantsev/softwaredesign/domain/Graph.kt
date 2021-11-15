package ru.itmo.ct.kubantsev.softwaredesign.domain

interface Graph {
    val size: Int

    fun getOutcomeEdges(vertex: Int): Set<Int>
}