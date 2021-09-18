import org.testng.Assert.*
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

private const val CAPACITY = 4

class LRUCacheTest {

    @Test(dataProvider = "lruCache")
    fun singleSetTest(lruCache: LRUCache<Int, Int>) {
        lruCache[1] = 1
        assertTrue(lruCache.containsKey(1))
        assertTrue(lruCache.containsValue(1))
        assertEquals(1, lruCache[1])
        assertEquals(1, lruCache.size)
    }

    @Test(dataProvider = "lruCache")
    fun manySetTest(lruCache: LRUCache<Int, Int>) {
        val goodKeys = listOf(1, 2, 4, 7)
        val badKeys = listOf(3, 5, 6, 8)
        for (key in goodKeys) {
            lruCache[key] = key + 7
        }

        for (key in goodKeys) {
            assertTrue(lruCache.containsKey(key))
            assertTrue(lruCache.containsValue(key + 7))
            assertEquals(key + 7, lruCache[key])
        }

        for (key in badKeys) {
            assertFalse(lruCache.containsKey(key))
            assertFalse(lruCache.containsValue(key + 7))
        }
        assertEquals(4, lruCache.size)
    }

    @Test
    fun rewriteTest() {
        val lruCache = LRUCache<Key, Int>()
        lruCache[Key(1)] = 1
        lruCache[Key(2)] = 2

        assertEquals(1, lruCache[Key(1)])
        assertEquals(2, lruCache[Key(2)])
        assertEquals(2, lruCache.size)

        lruCache[Key(1)] = 3
        lruCache[Key(4, 2)] = 4

        assertEquals(3, lruCache[Key(1)])
        assertEquals(2, lruCache[Key(2)])
        assertEquals(4, lruCache[Key(4, 2)])
        assertEquals(3, lruCache.size)
    }

    @Test(dataProvider = "lruCache")
    fun evictionTest(lruCache: LRUCache<Int, Int>) {
        for (key in 1..10) {
            lruCache[key] = key
        }
        for (key in 1..6) {
            assertFalse(lruCache.containsKey(key))
            assertEquals(null, lruCache[key])
        }
        for (key in 7..10) {
            assertTrue(lruCache.containsKey(key))
            assertEquals(key, lruCache[key])
        }
        for (key in 1..4) {
            lruCache[key] = key
        }
        lruCache[2]
        lruCache[3]
        lruCache[5] = 5
        lruCache[6] = 6
        assertEquals(null, lruCache[1])
        assertEquals(2, lruCache[2])
        assertEquals(3, lruCache[3])
        assertEquals(null, lruCache[4])
    }

    @Test(dataProvider = "lruCache", invocationCount = 10)
    fun evictionRandomTest(lruCache: LRUCache<Int, Int>) {
        println("Eviction random test")
        println("====================")
        println("operation:")
        val keys = 1..10
        val order = List(50) { keys.random() }
        for (i in order.indices) {
            val key = order[i]
            val lastElements = getLastElements(order.subList(0, i))
            if (lruCache[key] == null) {
                println("set $key")
                assertFalse(lastElements.contains(key))
                lruCache[key] = key
            } else {
                println("get $key")
                assertTrue(lastElements.contains(key))
            }
        }
        val lastElements = getLastElements(order)
        for (key in lastElements) {
            assertTrue(lruCache.containsKey(key))
            assertEquals(key, lruCache[key])
        }
        for (key in keys - lastElements) {
            assertFalse(lruCache.containsKey(key))
            assertEquals(null, lruCache[key])
        }
        assertEquals(CAPACITY, lruCache.size)
        println("====================")
        println("Test done!")
        println()
    }

    private fun getLastElements(order: List<Int>): Set<Int> {
        val lastElements = mutableSetOf<Int>()
        for (key in order.reversed()) {
            if (lastElements.size == CAPACITY) {
                break
            }
            lastElements.add(key)
        }
        return lastElements
    }

    @DataProvider
    fun lruCache() = arrayOf(arrayOf(LRUCache<Int, Int>(CAPACITY)))

    data class Key(private val key: Int, private val hash: Int = key) {
        override fun hashCode(): Int {
            return hash
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Key

            if (key != other.key) return false
            if (hash != other.hash) return false

            return true
        }
    }
}