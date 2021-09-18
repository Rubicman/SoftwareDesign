private const val DEFAULT_CAPACITY = 8

class LRUCache<K, V>(private val capacity: Int = DEFAULT_CAPACITY) {

    private val map = HashMap<K, Node<V>>(capacity)
    private var head: Node<V>? = null
    private var tail: Node<V>? = null

    init {
        if (capacity <= 0) {
            throw IllegalArgumentException("capacity must be positive")
        }
    }

    operator fun set(key: K, value: V) {
        TODO()
    }

    operator fun get(key: K): V? {
        TODO()
    }

    fun containsKey(key: K): Boolean {
        TODO()
    }

    fun containsValue(value: V): Boolean {
        TODO()
    }

    var size: Int = 0
        private set

    private data class Node<V>(
        val value: V,
        var previous: Node<V>? = null,
        var next: Node<V>? = null
    )
}