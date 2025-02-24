package lesson5

import lesson5.impl.GraphBuilder
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

abstract class AbstractGraphTests {

    private fun Graph.Edge.isNeighbour(other: Graph.Edge): Boolean {
        return begin == other.begin || end == other.end || begin == other.end || end == other.begin
    }

    private fun List<Graph.Edge>.assert(shouldExist: Boolean, graph: Graph) {
        val edges = graph.edges
        if (shouldExist) {
            assertEquals(edges.size, size, "Euler loop should traverse all edges")
        } else {
            assertTrue(isEmpty(), "Euler loop should not exist")
        }
        for (edge in this) {
            assertTrue(edge in edges, "Edge $edge is not inside graph")
        }
        for (i in 0 until size - 1) {
            assertTrue(this[i].isNeighbour(this[i + 1]), "Edges ${this[i]} & ${this[i + 1]} are not incident")
        }
        if (size > 1) {
            assertTrue(this[0].isNeighbour(this[size - 1]), "Edges ${this[0]} & ${this[size - 1]} are not incident")
        }
    }

    fun findEulerLoop(findEulerLoop: Graph.() -> List<Graph.Edge>) {
        val emptyGraph = GraphBuilder().build()
        val emptyLoop = emptyGraph.findEulerLoop()
        assertTrue(emptyLoop.isEmpty(), "Euler loop should be empty for the empty graph")
        val simpleGraph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            addConnection(a, b)
        }.build()
        val simpleLoop = simpleGraph.findEulerLoop()
        simpleLoop.assert(shouldExist = false, graph = simpleGraph)
        val unconnected = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            addConnection(a, b)
            addConnection(c, d)
        }.build()
        val unconnectedLoop = unconnected.findEulerLoop()
        unconnectedLoop.assert(shouldExist = false, graph = unconnected)
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(a, c)
        }.build()
        val loop = graph.findEulerLoop()
        loop.assert(shouldExist = true, graph = graph)
        val graph2 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(a, e)
            addConnection(d, k)
            addConnection(e, j)
            addConnection(j, k)
            addConnection(b, f)
            addConnection(c, i)
            addConnection(f, i)
            addConnection(b, g)
            addConnection(g, h)
            addConnection(h, c)
        }.build()
        val loop2 = graph2.findEulerLoop()
        loop2.assert(shouldExist = true, graph = graph2)
        // Seven bridges of Koenigsberg
        //    A1 -- A2 ---
        //    |      |    |
        //    B1 -- B2 -- C
        //    |     |     |
        //    D1 -- D2 ---
        val graph3 = GraphBuilder().apply {
            val a1 = addVertex("A1")
            val a2 = addVertex("A2")
            val b1 = addVertex("B1")
            val b2 = addVertex("B2")
            val c = addVertex("C")
            val d1 = addVertex("D1")
            val d2 = addVertex("D2")
            addConnection(a1, a2)
            addConnection(b1, b2)
            addConnection(d1, d2)
            addConnection(a1, b1)
            addConnection(b1, d1)
            addConnection(a2, b2)
            addConnection(b2, d2)
            addConnection(a2, c)
            addConnection(b2, c)
            addConnection(d2, c)
        }.build()
        val loop3 = graph3.findEulerLoop()
        loop3.assert(shouldExist = false, graph = graph3)
    }

    fun minimumSpanningTree(minimumSpanningTree: Graph.() -> Graph) {
        val emptyGraph = GraphBuilder().build()
        assertTrue(emptyGraph.minimumSpanningTree().edges.isEmpty())
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(a, c)
        }.build()
        val tree = graph.minimumSpanningTree()
        assertEquals(2, tree.edges.size)
        assertEquals(2, tree.findBridges().size)
        val graph2 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(a, e)
            addConnection(d, k)
            addConnection(e, j)
            addConnection(j, k)
            addConnection(b, f)
            addConnection(c, i)
            addConnection(f, i)
            addConnection(b, g)
            addConnection(g, h)
            addConnection(h, c)
        }.build()
        val tree2 = graph2.minimumSpanningTree()
        assertEquals(10, tree2.edges.size)
        assertEquals(10, tree2.findBridges().size)
        // Cross
        val graph3 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            addConnection(a, e)
            addConnection(b, e)
            addConnection(c, e)
            addConnection(d, e)
        }.build()
        val tree3 = graph3.minimumSpanningTree()
        assertEquals(4, tree3.edges.size)
        assertEquals(4, tree3.findBridges().size)
    }

    fun largestIndependentVertexSet(largestIndependentVertexSet: Graph.() -> Set<Graph.Vertex>) {
        val emptyGraph = GraphBuilder().build()
        assertTrue(emptyGraph.largestIndependentVertexSet().isEmpty())
        val simpleGraph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            addConnection(a, b)
        }.build()
        assertEquals(
            setOf(simpleGraph["A"]),
            simpleGraph.largestIndependentVertexSet()
        )
        val unconnected = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            addConnection(a, b)
            addConnection(c, d)
            addConnection(d, e)
        }.build()
        assertEquals(
            setOf(unconnected["A"], unconnected["C"], unconnected["E"]),
            unconnected.largestIndependentVertexSet()
        )
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            addConnection(a, b)
            addConnection(a, c)
            addConnection(b, d)
            addConnection(c, e)
            addConnection(c, f)
            addConnection(b, g)
            addConnection(d, i)
            addConnection(g, h)
            addConnection(h, j)
        }.build()
        assertEquals(
            setOf(graph["A"], graph["D"], graph["E"], graph["F"], graph["G"], graph["J"]),
            graph.largestIndependentVertexSet()
        )
        val cross = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            addConnection(a, e)
            addConnection(b, e)
            addConnection(c, e)
            addConnection(d, e)
        }.build()
        assertEquals(
            setOf(cross["A"], cross["B"], cross["C"], cross["D"]),
            cross.largestIndependentVertexSet()
        )
        //    A           E
        //    |           |
        //    B --- C --- D
        //    |           |
        //    H --- G --- F
        //    |           |
        //    I --- J --- K
        //    |           |
        //    N --- M --- L
        //    |           |
        //    O --- P --- Q
        //    |           |
        //    T --- S --- R
        //    |           |
        //    U           V
        val stairs = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            val l = addVertex("L")
            val m = addVertex("M")
            val n = addVertex("N")
            val o = addVertex("O")
            val p = addVertex("P")
            val q = addVertex("Q")
            val r = addVertex("R")
            val s = addVertex("S")
            val t = addVertex("T")
            val u = addVertex("U")
            val v = addVertex("V")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(d, e)
            addConnection(d, f)
            addConnection(f, g)
            addConnection(g, h)
            addConnection(h, b)
            addConnection(h, i)
            addConnection(i, j)
            addConnection(j, k)
            addConnection(k, f)
            addConnection(k, l)
            addConnection(l, m)
            addConnection(m, n)
            addConnection(n, i)
            addConnection(n, o)
            addConnection(o, p)
            addConnection(p, q)
            addConnection(q, l)
            addConnection(q, r)
            addConnection(r, v)
            addConnection(r, s)
            addConnection(s, t)
            addConnection(t, o)
            addConnection(t, u)
        }.build()
        assertFailsWith<IllegalArgumentException> {
            stairs.largestIndependentVertexSet()
        }
        //    A --- B
        //    |     |
        //    D --- C
        val cycle = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(d, a)
        }.build()
        assertFailsWith<IllegalArgumentException> {
            cycle.largestIndependentVertexSet()
        }
        //    A --- B --- C
        //    |     |     |
        //    D --- E --- F
        //    |     |     |
        //    G --- H --- I
        val cycle2 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            addConnection(a, b)
            addConnection(a, d)
            addConnection(b, c)
            addConnection(b, e)
            addConnection(c, f)
            addConnection(d, e)
            addConnection(d, g)
            addConnection(e, f)
            addConnection(e, h)
            addConnection(f, i)
            addConnection(g, h)
            addConnection(h, i)
        }.build()
        assertFailsWith<IllegalArgumentException> {
            cycle2.largestIndependentVertexSet()
        }
        //    A           E
        //    |           |
        //    B --- C --- D
        //                |
        //    H --- G --- F
        //    |
        //    I --- J --- K
        //                |
        //    N --- M --- L
        //    |
        //    O --- P --- Q
        //                |
        //    T --- S --- R
        //    |           |
        //    U           V
        val snake = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            val l = addVertex("L")
            val m = addVertex("M")
            val n = addVertex("N")
            val o = addVertex("O")
            val p = addVertex("P")
            val q = addVertex("Q")
            val r = addVertex("R")
            val s = addVertex("S")
            val t = addVertex("T")
            val u = addVertex("U")
            val v = addVertex("V")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(d, e)
            addConnection(d, f)
            addConnection(f, g)
            addConnection(g, h)
            addConnection(h, i)
            addConnection(i, j)
            addConnection(j, k)
            addConnection(k, l)
            addConnection(l, m)
            addConnection(m, n)
            addConnection(n, o)
            addConnection(o, p)
            addConnection(p, q)
            addConnection(q, r)
            addConnection(r, v)
            addConnection(r, s)
            addConnection(s, t)
            addConnection(t, u)
        }.build()
        assertEquals(
            setOf(
                snake["A"],
                snake["C"],
                snake["E"],
                snake["F"],
                snake["H"],
                snake["J"],
                snake["L"],
                snake["N"],
                snake["P"],
                snake["S"],
                snake["U"],
                snake["V"]
            ),
            snake.largestIndependentVertexSet()
        )
        //    A           E
        //    |           |
        //    B --- C --- D
        //                |
        //    H --- G --- F
        //    |
        //    I --- J --- K
        //                |
        //    N --- M --- L
        //    |
        //    O --- P --- Q
        //                |
        //    T --- S --- R
        //    |           |
        //    U           V
        val snake2 = GraphBuilder().apply {
            val g = addVertex("G")
            val k = addVertex("K")
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val l = addVertex("L")
            val m = addVertex("M")
            val n = addVertex("N")
            val o = addVertex("O")
            val p = addVertex("P")
            val q = addVertex("Q")
            val r = addVertex("R")
            val s = addVertex("S")
            val t = addVertex("T")
            val u = addVertex("U")
            val v = addVertex("V")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(d, e)
            addConnection(d, f)
            addConnection(f, g)
            addConnection(g, h)
            addConnection(h, i)
            addConnection(i, j)
            addConnection(j, k)
            addConnection(k, l)
            addConnection(l, m)
            addConnection(m, n)
            addConnection(n, o)
            addConnection(o, p)
            addConnection(p, q)
            addConnection(q, r)
            addConnection(r, v)
            addConnection(r, s)
            addConnection(s, t)
            addConnection(t, u)
        }.build()
        assertEquals(
            setOf(
                snake2["A"],
                snake2["C"],
                snake2["E"],
                snake2["G"],
                snake2["I"],
                snake2["K"],
                snake2["M"],
                snake2["O"],
                snake2["Q"],
                snake2["S"],
                snake2["U"],
                snake2["V"]
            ),
            snake2.largestIndependentVertexSet()
        )

    }

    fun longestSimplePath(longestSimplePath: Graph.() -> Path) {
        val emptyGraph = GraphBuilder().build()
        assertEquals(0, emptyGraph.longestSimplePath().length)

        val unconnected = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            addConnection(a, b)
            addConnection(c, d)
            addConnection(d, e)
        }.build()
        val longestUnconnectedPath = unconnected.longestSimplePath()
        assertEquals(2, longestUnconnectedPath.length)

        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(a, c)
        }.build()
        val longestPath = graph.longestSimplePath()
        assertEquals(2, longestPath.length)

        val graph2 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(a, e)
            addConnection(d, k)
            addConnection(e, j)
            addConnection(j, k)
            addConnection(b, f)
            addConnection(c, i)
            addConnection(f, i)
            addConnection(b, g)
            addConnection(g, h)
            addConnection(h, c)
        }.build()
        val longestPath2 = graph2.longestSimplePath()
        assertEquals(10, longestPath2.length)
        // Seven bridges of Koenigsberg
        //    A1 -- A2 ---
        //    |      |    |
        //    B1 -- B2 -- C
        //    |     |     |
        //    D1 -- D2 ---
        val graph3 = GraphBuilder().apply {
            val a1 = addVertex("A1")
            val a2 = addVertex("A2")
            val b1 = addVertex("B1")
            val b2 = addVertex("B2")
            val c = addVertex("C")
            val d1 = addVertex("D1")
            val d2 = addVertex("D2")
            addConnection(a1, a2)
            addConnection(b1, b2)
            addConnection(d1, d2)
            addConnection(a1, b1)
            addConnection(b1, d1)
            addConnection(a2, b2)
            addConnection(b2, d2)
            addConnection(a2, c)
            addConnection(b2, c)
            addConnection(d2, c)
        }.build()
        val longestPath3 = graph3.longestSimplePath()
        assertEquals(6, longestPath3.length)
        //    A     B     C
        //
        //    D     E     F
        //
        //    G     H     I
        val graph4 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
        }.build()
        val longestPath4 = graph4.longestSimplePath()
        assertEquals(0, longestPath4.length)
        //    A --- B --- C
        //    |     |     |
        //    D --- E --- F
        //    |     |     |
        //    G --- H --- I
        val graph5 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            addConnection(a, b)
            addConnection(a, d)
            addConnection(b, c)
            addConnection(b, e)
            addConnection(c, f)
            addConnection(d, e)
            addConnection(d, g)
            addConnection(e, f)
            addConnection(e, h)
            addConnection(f, i)
            addConnection(g, h)
            addConnection(h, i)
        }.build()
        val longestPath5 = graph5.longestSimplePath()
        assertEquals(8, longestPath5.length)
    }

}