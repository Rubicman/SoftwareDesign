private const val DEFAULT_CAPACITY = 8

class LRUCache<K, V>(private val capacity: Int = DEFAULT_CAPACITY) {

    private val map = HashMap<K, Node<K, V>>(capacity)
    private var head: Node<K, V>? = null
    private var tail: Node<K, V>? = null

    init {
        if (capacity <= 0) {
            throw IllegalArgumentException("Capacity must be positive")
        }
    }

    operator fun set(key: K, value: V) {
        val node = Node(key, value)
        addNode(node, head)
        map[key]?.also { removeNode(it) }
        if (size > capacity) {
            assert(tail != null) { "Tail can not null when cache is not empty" }
            map.remove(tail!!.key)
            removeNode(tail!!)
        }
        map[key] = node
    }

    operator fun get(key: K): V? {
        val node = map[key] ?: return null
        removeNode(node)
        addNode(node, head)
        return node.value
    }

    fun containsKey(key: K): Boolean = map.containsKey(key)

    fun containsValue(value: V): Boolean {
        var node = head
        while (node != null) {
            if (node.value == value) {
                return true
            }
            node = node.next
        }
        return false
    }

    private fun addNode(node: Node<K, V>, next: Node<K, V>?) {
        if (next == null) {
            assert(size == 0) { "Previous can be null only when cache is empty" }
            head = node
            tail = node
        } else {
            val previous = next.previous

            if (previous != null) {
                assert(head != next) { "Node can't be head and have a previous" }
                node.previous = previous
                previous.next = node
            } else {
                assert(head == next) { "If node has not a previous it must be the head" }
                head = node
            }

            node.next = next
            next.previous = node
        }
        size++
    }

    private fun removeNode(node: Node<K, V>) {
        val previous = node.previous
        val next = node.next
        if (previous == null) {
            assert(head == node) { "If node has not a previous it must be the head" }
            head = node.next
        } else {
            assert(head != node) { "Node can't be a head and have a previous" }
            previous.next = node.next
        }
        if (next == null) {
            assert(tail == node) { "If node has not a next it must be the tail" }
            tail = node.previous
        } else {
            assert(tail != node) { "Node can't be a tail and have a next" }
            next.previous = node.previous
        }
        node.previous = null
        node.next = null

        size--
    }

    var size: Int = 0
        private set

    private data class Node<K, V>(
        val key: K,
        val value: V,
        var previous: Node<K, V>? = null,
        var next: Node<K, V>? = null
    )
}