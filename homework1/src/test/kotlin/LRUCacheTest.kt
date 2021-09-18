import org.testng.Assert.*
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import kotlin.test.assertContains

private const val CAPACITY = 4

class LRUCacheTest {

    @Test(dataProvider = "lruCache")
    fun singleSetTest(lruCache: LRUCache<Int, Int>) {
        lruCache[1] = 1
        assertTrue(lruCache.containsKey(1))
        assertTrue(lruCache.containsValue(1))
        assertEquals(1, lruCache[1])
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
            assertEquals(key + 7, lruCache[key])
        }

        for (key in badKeys) {
            assertFalse(lruCache.containsKey(key))
        }
    }

    @Test
    fun rewriteTest() {
        val lruCache = LRUCache<Key, Int>()
        lruCache[Key(1)] = 1
        lruCache[Key(2)] = 2

        assertEquals(1, lruCache[Key(1)])
        assertEquals(2, lruCache[Key(2)])

        lruCache[Key(1)] = 3
        lruCache[Key(4, 2)] = 4

        assertEquals(3, lruCache[Key(1)])
        assertEquals(2, lruCache[Key(2)])
        assertEquals(4, lruCache[Key(4)])
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
        for (key in 1..10) {
            lruCache[key] = key
        }
    }

    @Test(dataProvider = "lruCache", invocationCount = 10)
    fun evictionRandomTest(lruCache: LRUCache<Int, Int>) {
        val keys = 1..10
        val order = List(50) {keys.random()}
        for (i in order.indices) {
            val key = order[i]
            val lastElements = getLastElements(order.subList(0, i))
            if (lruCache[key] == null) {
                lruCache[key] = key
                assertFalse(lastElements.contains(key))
            } else {
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
    }

    private fun getLastElements(order: List<Int>): Set<Int> {
        val lastElements = mutableSetOf<Int>()
        for(key in order.reversed()) {
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

        fun equals(other: Key) =
            key == other.key && hash == other.hash

        override fun equals(other: Any?) = false
    }
}