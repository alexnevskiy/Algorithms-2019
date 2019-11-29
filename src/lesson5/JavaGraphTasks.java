package lesson5;

import kotlin.NotImplementedError;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {
    /**
     * Эйлеров цикл.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     *
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     */
    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ:
     *
     *      G    H
     *      |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     */
    public static Graph minimumSpanningTree(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     *
     * Дан граф без циклов (получатель), например
     *
     *      G -- H -- J
     *      |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     *
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     *
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     *
     * В данном случае ответ (A, E, F, D, G, J)
     *
     * Если на входе граф с циклами, бросить IllegalArgumentException
     *
     * Эта задача может быть зачтена за пятый и шестой урок одновременно
     *
     * Алгоритм:
     * Проходимся по всем рёбрам графа. Сначала создаём сэт из всех ребёр графа и два пустых сета,
     * где будут храниться два возможных максимально независимых множества вершин (чётные и нечётные множества).
     * Реализованы эти два сета через LinkedHashSet, так как при нескольких самых больших множествах,
     * приоритет имеет то из них, в котором вершины расположены раньше во множестве graph.getVertices(), начиная с первых.
     * Далее при проходе по всем рёбрам графа создаём переменные, реализующие начало и конец ребра.
     * Далее делаем проверку на наличие цикла в графе и заполняем сеты вершинами.
     * В конце выводится сет с большим количеством вершин.
     */
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        Set<Graph.Edge> edges = graph.getEdges();
        Set<Graph.Vertex> evenVertices = new LinkedHashSet<>();
        Set<Graph.Vertex> unevenVertices = new LinkedHashSet<>();
        for (Graph.Edge edge : edges) {
            Graph.Vertex begin = edge.getBegin();
            Graph.Vertex end = edge.getEnd();
            if (evenVertices.contains(begin) && unevenVertices.contains(end)
                    || evenVertices.contains(end) && unevenVertices.contains(begin))
                throw new IllegalArgumentException();
            if (!unevenVertices.contains(begin) && !evenVertices.contains(end)) {
                evenVertices.add(begin);
                unevenVertices.add(end);
            } else if (unevenVertices.contains(begin)) evenVertices.add(end);
            else unevenVertices.add(begin);
        }
        if (evenVertices.size() >= unevenVertices.size()) return evenVertices;
        else return unevenVertices;
    }  //  Вывод: Т=O(e), R=O(v), где v - количество вершин в графе, e - количество рёбер в графе

    /**
     * Наидлиннейший простой путь.
     * Сложная
     *
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     *
     * Алгоритм:
     * Рассматриваем все возможные пути в графе. Сначала добавляем в очередь все вершины графа,
     * далее рассматриваем все возможные пути: если длина текущего пути больше, чем у самого длинного,
     * то запоминаем текущий путь как самый длинный и присваиваем его длину. Затем для вершины
     * в конце текущего пути ищем соседей, которые ещё не были добавлены в данный путь, и добавляем их соответственно.
     * Таким образом, рассматриваются все возможные варианты путей в графе, и запоминается самый длинный из них.
     */
    public static Path longestSimplePath(Graph graph) {
        Queue<Path> queueOfPaths = new LinkedList<>();
        Path longestSimplePath = new Path();
        int length = -1;
        for (Graph.Vertex vertex : graph.getVertices()) queueOfPaths.offer(new Path(vertex));
        while (!queueOfPaths.isEmpty()) {
            Path path = queueOfPaths.poll();
            if (path.getLength() > length) {
                longestSimplePath = path;
                length = path.getLength();
            }
            for (Graph.Vertex neighbour : graph.getNeighbors(path.getVertices().get(path.getLength()))) {
                if (!path.contains(neighbour)) queueOfPaths.offer(new Path(path, graph, neighbour));
            }
        }
        return longestSimplePath;
    }  //  Вывод: Т=O(v!), R=O(v!), где v - количество вершин в графе
}
