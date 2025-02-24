package lesson3

import java.util.*
import kotlin.test.*

abstract class AbstractHeadTailTest {
    private lateinit var tree: SortedSet<Int>
    private lateinit var randomTree: SortedSet<Int>
    private val randomTreeSize = 1000
    private val randomValues = mutableListOf<Int>()

    protected fun fillTree(create: () -> SortedSet<Int>) {
        this.tree = create()
        //В произвольном порядке добавим числа от 1 до 10
        tree.add(5)
        tree.add(1)
        tree.add(2)
        tree.add(7)
        tree.add(9)
        tree.add(10)
        tree.add(8)
        tree.add(4)
        tree.add(3)
        tree.add(6)

        this.randomTree = create()
        val random = Random()
        for (i in 0 until randomTreeSize) {
            val randomValue = random.nextInt(randomTreeSize) + 1
            if (randomTree.add(randomValue)) {
                randomValues.add(randomValue)
            }
        }
    }


    protected fun doHeadSetTest() {
        var set: SortedSet<Int> = tree.headSet(5)
        assertEquals(true, set.contains(1))
        assertEquals(true, set.contains(2))
        assertEquals(true, set.contains(3))
        assertEquals(true, set.contains(4))
        assertEquals(false, set.contains(5))
        assertEquals(false, set.contains(6))
        assertEquals(false, set.contains(7))
        assertEquals(false, set.contains(8))
        assertEquals(false, set.contains(9))
        assertEquals(false, set.contains(10))


        set = tree.headSet(127)
        for (i in 1..10)
            assertEquals(true, set.contains(i))

    }

    protected fun doTailSetTest() {
        var set: SortedSet<Int> = tree.tailSet(5)
        assertEquals(false, set.contains(1))
        assertEquals(false, set.contains(2))
        assertEquals(false, set.contains(3))
        assertEquals(false, set.contains(4))
        assertEquals(true, set.contains(5))
        assertEquals(true, set.contains(6))
        assertEquals(true, set.contains(7))
        assertEquals(true, set.contains(8))
        assertEquals(true, set.contains(9))
        assertEquals(true, set.contains(10))

        set = tree.tailSet(-128)
        for (i in 1..10)
            assertEquals(true, set.contains(i))

    }

    protected fun doHeadSetRelationTest() {
        val set: SortedSet<Int> = tree.headSet(7)
        assertEquals(6, set.size)
        assertEquals(10, tree.size)
        tree.add(0)
        assertTrue(set.contains(0))
        set.add(-2)
        assertTrue(tree.contains(-2))
        tree.add(12)
        assertFalse(set.contains(12))
        assertFailsWith<IllegalArgumentException> { set.add(8) }
        assertEquals(8, set.size)
        assertEquals(13, tree.size)
    }

    protected fun doHeadSetRemoveTest() {  //  Продолжаем работать с тем же деревом из doHeadSetRelationTest (tree.size = 13)
        val set: SortedSet<Int> = tree.headSet(9)
        assertEquals(10, set.size)
        assertEquals(13, tree.size)
        tree.add(-10)
        assertTrue(set.contains(-10))
        set.add(-20)
        assertTrue(tree.contains(-20))
        tree.add(30)
        assertFalse(set.contains(30))
        assertFailsWith<IllegalArgumentException> { set.add(9) }
        assertEquals(12, set.size)
        assertEquals(16, tree.size)
        tree.remove(12)
        assertFalse(tree.contains(12))
        set.remove(5)
        assertFalse(set.contains(5))
        tree.remove(4)
        assertFalse(set.contains(4))
        set.remove(1)
        assertFalse(tree.contains(1))
        assertFailsWith<IllegalArgumentException> { set.add(12) }
        assertEquals(9, set.size)
        assertEquals(12, tree.size)
    }

    protected fun doTailSetRelationTest() {
        val set: SortedSet<Int> = tree.tailSet(4)
        assertEquals(7, set.size)
        assertEquals(10, tree.size)
        tree.add(12)
        assertTrue(set.contains(12))
        set.add(42)
        assertTrue(tree.contains(42))
        tree.add(0)
        assertFalse(set.contains(0))
        assertFailsWith<IllegalArgumentException> { set.add(-2) }
        assertEquals(9, set.size)
        assertEquals(13, tree.size)
    }

    protected fun doTailSetRemoveTest() {  //  Продолжаем работать с тем же деревом из doTailSetRelationTest (tree.size = 13)
        val set: SortedSet<Int> = tree.tailSet(7)
        assertEquals(6, set.size)
        assertEquals(13, tree.size)
        tree.add(100)
        assertTrue(set.contains(100))
        set.add(150)
        assertTrue(tree.contains(150))
        tree.add(-33)
        assertFalse(set.contains(-33))
        assertFailsWith<IllegalArgumentException> { set.add(6) }
        assertEquals(8, set.size)
        assertEquals(16, tree.size)
        tree.remove(0)
        assertFalse(tree.contains(0))
        set.remove(10)
        assertFalse(set.contains(10))
        tree.remove(7)
        assertFalse(set.contains(7))
        set.remove(42)
        assertFalse(tree.contains(42))
        assertFailsWith<IllegalArgumentException> { set.add(0) }
        assertEquals(5, set.size)
        assertEquals(12, tree.size)
    }

    protected fun doSubSetTest() {
        val smallSet: SortedSet<Int> = tree.subSet(3, 8)
        assertEquals(false, smallSet.contains(1))
        assertEquals(false, smallSet.contains(2))
        assertEquals(true, smallSet.contains(3))
        assertEquals(true, smallSet.contains(4))
        assertEquals(true, smallSet.contains(5))
        assertEquals(true, smallSet.contains(6))
        assertEquals(true, smallSet.contains(7))
        assertEquals(false, smallSet.contains(8))
        assertEquals(false, smallSet.contains(9))
        assertEquals(false, smallSet.contains(10))

        assertFailsWith<IllegalArgumentException> { smallSet.add(2) }
        assertFailsWith<IllegalArgumentException> { smallSet.add(9) }

        val allSet = tree.subSet(-128, 128)
        for (i in 1..10)
            assertEquals(true, allSet.contains(i))

        val random = Random()
        val toElement = random.nextInt(randomTreeSize) + 1
        val fromElement = random.nextInt(toElement - 1) + 1

        val randomSubset = randomTree.subSet(fromElement, toElement)
        randomValues.forEach { element ->
            assertEquals(element in fromElement until toElement, randomSubset.contains(element))
        }
    }

    protected fun doSubSetRelationTest() {
        val set: SortedSet<Int> = tree.subSet(2, 15)
        assertEquals(9, set.size)
        assertEquals(10, tree.size)
        tree.add(11)
        assertTrue(set.contains(11))
        set.add(14)
        assertTrue(tree.contains(14))
        tree.add(0)
        assertFalse(set.contains(0))
        tree.add(15)
        assertFalse(set.contains(15))
        assertFailsWith<IllegalArgumentException> { set.add(1) }
        assertFailsWith<IllegalArgumentException> { set.add(20) }
        assertEquals(11, set.size)
        assertEquals(14, tree.size)
    }

    protected fun doSubSetRemoveTest() {  //  Продолжаем работать с тем же деревом из doSubSetRelationTest (tree.size = 14)
        val smallSet: SortedSet<Int> = tree.subSet(4, 8)
        assertEquals(false, smallSet.contains(1))
        assertEquals(false, smallSet.contains(2))
        assertEquals(false, smallSet.contains(3))
        assertEquals(true, smallSet.contains(4))
        assertEquals(true, smallSet.contains(5))
        assertEquals(true, smallSet.contains(6))
        assertEquals(true, smallSet.contains(7))
        assertEquals(false, smallSet.contains(8))
        assertEquals(false, smallSet.contains(9))
        assertEquals(false, smallSet.contains(10))

        assertFailsWith<IllegalArgumentException> { smallSet.add(2) }
        assertFailsWith<IllegalArgumentException> { smallSet.add(9) }
        assertEquals(false, smallSet.remove(10))
        assertEquals(false, smallSet.remove(8))

        val set: SortedSet<Int> = tree.subSet(5, 15)
        assertEquals(8, set.size)
        assertEquals(14, tree.size)
        set.remove(10)
        assertFalse(set.contains(10))
        tree.remove(5)
        assertFalse(tree.contains(5))
        set.remove(8)
        assertFalse(tree.contains(8))
        tree.remove(7)
        assertFalse(set.contains(7))
        assertFailsWith<IllegalArgumentException> { set.add(1) }
        assertFailsWith<IllegalArgumentException> { set.add(20) }
        assertEquals(false, set.remove(4))
        assertEquals(false, set.remove(15))
        assertEquals(4, set.size)
        assertEquals(10, tree.size)
    }  //  Все тесты на удаление элементов из поддерева подходят и для всех реализованных до этого методов

    protected fun testIterator(create: () -> CheckableSortedSet<Int>) {
        val random = Random()
        for (iteration in 1..20) {
            val list = mutableListOf<Int>()
            for (i in 1..20) {
                list.add(random.nextInt(100))
            }
            val treeSet = TreeSet<Int>()
            val binarySet = create()
            assertFalse(
                binarySet.subSet(0, 100).iterator().hasNext(),
                "Iterator of empty set should not have next element"
            )
            for (element in list) {
                treeSet += element
                binarySet += element
            }
            val randomSize = random.nextInt(100)
            val treeIt = treeSet.subSet(0, randomSize).iterator()
            val binaryIt = binarySet.subSet(0, randomSize).iterator()
            println("Traversing $list")
            while (binaryIt.hasNext()) {
                assertEquals(treeIt.next(), binaryIt.next(), "Incorrect iterator state while iterating $treeSet")
            }
            val iterator1 = binarySet.subSet(0, randomSize).iterator()
            val iterator2 = binarySet.subSet(0, randomSize).iterator()
            println("Consistency check for hasNext $list")
            // hasNext call should not affect iterator position
            while (iterator1.hasNext()) {
                assertEquals(
                    iterator2.next(), iterator1.next(),
                    "Call of iterator.hasNext() changes its state while iterating $treeSet"
                )
            }
        }
    }

    protected fun testIteratorRemove(create: () -> CheckableSortedSet<Int>) {
        val random = Random()
        for (iteration in 1..100) {
            val list = mutableListOf<Int>()
            for (i in 1..20) {
                list.add(random.nextInt(100))
            }
            val treeSet = TreeSet<Int>()
            val binarySet = create()
            for (element in list) {
                treeSet += element
                binarySet += element
            }
            val toRemove = list[random.nextInt(list.size)]
            treeSet.remove(toRemove)
            println("Removing $toRemove from $list")
            val iterator = binarySet.subSet(0, toRemove + 1).iterator()
            var counter = binarySet.subSet(0, toRemove + 1).size
            while (iterator.hasNext()) {
                val element = iterator.next()
                counter--
                print("$element ")
                if (element == toRemove) {
                    iterator.remove()
                }
            }
            assertEquals(
                0, counter,
                "Iterator.remove() of $toRemove from $list changed iterator position: " +
                        "we've traversed a total of ${binarySet.size - counter} elements instead of ${binarySet.size}"
            )
            println()
            assertEquals<SortedSet<*>>(treeSet, binarySet, "After removal of $toRemove from $list")
            assertEquals(treeSet.size, binarySet.size, "Size is incorrect after removal of $toRemove from $list")
            for (element in list) {
                val inn = element != toRemove
                assertEquals(
                    inn, element in binarySet,
                    "$element should be ${if (inn) "in" else "not in"} tree"
                )
            }
            assertTrue(binarySet.checkInvariant(), "Binary tree invariant is false after tree.iterator().remove()")
        }
    }
}