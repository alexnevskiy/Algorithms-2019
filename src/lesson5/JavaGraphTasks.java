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
     * Код можно условно разделить на две части:
     * 1. Поиск в графе цикла с выбрасыванием IllegalArgumentException
     * 2. Сам поиск максимального независимого множества вершин в графе с выводом ответа
     *
     * Алгоритм для 1 части кода:
     * Проходимся по всем рёбрам графа. Сначала создаём сет из всех ребёр графа и два пустых сета,
     * где будут храниться два возможных максимально независимых множества вершин (чётные и нечётные множества).
     * Далее при проходе по всем рёбрам графа создаём переменные, реализующие начало и конец ребра.
     * Затем делаем проверку на наличие цикла в графе и заполняем сеты вершинами.
     *
     * Алгоритм для 2 части кода:
     * Проходимся по всем вершинам графа. Создаём два сета для подходящих вершин и неподходящих соответственно.
     * В каждой итерации цикла будем отталкиваться от вершины, с которой мы работаем на данной итерации.
     * Далее проходимся по всем вершинам ещё раз и сравниваем, является ли другая вершина (other) соседом основной
     * вершины (vertex) и не присутствует ли она в сете из неподходящих вершин. Если по всем критериями other
     * проходит, то она добавляется в сет из подходящих вершин, а все её соседи в сет из неподходящих.
     * Так проходимся по всем другим вершинам и в конце добавляем итоговый сет в лист для дальнейшей обработки ответа.
     *
     * После обхода всего графа выявляем подходящий сет с максимальным независимым множеством вершин.
     * Делаем проверку по размеру сета:
     * Если размер нового сета больше, чем размер самого большого, то переприсваиваем сет и
     * переходим к следующему;
     * Если же размеры равны, то создаём итераторы для сетов и экземпляр вспомогательного класса Comparator.
     * Далее проходимся по всем вершинам в сете и сравниваем их индексы во множестве this.vertices и если
     * элемент в новом сете находится раньше, чем в самом большом, то переприсваиваем сет.
     */
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        List<Set<Graph.Vertex>> allPossibleSets = new ArrayList<>();
        Set<Graph.Vertex> largestIndependentVertexSet = new LinkedHashSet<>();

        Set<Graph.Edge> edges = graph.getEdges();
        Set<Graph.Vertex> evenVertices = new HashSet<>();
        Set<Graph.Vertex> unevenVertices = new HashSet<>();
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

        for (Graph.Vertex vertex : graph.getVertices()) {
            Set<Graph.Vertex> matchingVertices = new LinkedHashSet<>();
            Set<Graph.Vertex> remainingVertices = new HashSet<>();
            for (Graph.Vertex other : graph.getVertices()) {
                if (!graph.getNeighbors(vertex).contains(other) && !remainingVertices.contains(other)) {
                    matchingVertices.add(other);
                    remainingVertices.addAll(graph.getNeighbors(other));
                }
            }
            allPossibleSets.add(matchingVertices);
        }
        for (Set<Graph.Vertex> set : allPossibleSets) {
            if (largestIndependentVertexSet.size() < set.size()) {
                largestIndependentVertexSet = set;
                continue;
            }
            if (largestIndependentVertexSet.size() == set.size()) {
                Iterator largestIterator = largestIndependentVertexSet.iterator();
                Iterator currentIterator = set.iterator();
                Comparator comparator = new Comparator(graph);
                while (largestIterator.hasNext()) {
                    Graph.Vertex largestVertex = (Graph.Vertex) largestIterator.next();
                    Graph.Vertex currentVertex = (Graph.Vertex) currentIterator.next();
                    if (comparator.compare(largestVertex, currentVertex) < 0) break;
                    if (comparator.compare(largestVertex, currentVertex) > 0) largestIndependentVertexSet = set;
                }
            }
        }
        return largestIndependentVertexSet;
    }  //  Вывод: Т=O(v^2), R=O(a), где v - количество вершин в графе, a - все возможные независимые множества вершин
    // в графе (allPossibleSets)

    /**
     * Вспомогательный класс, реализующий компаратор для Graph.Vertex
     *
     * Метод indexMap() добавляет в map вершины с их индексами во множестве вершин this.vertices
     */
    private static class Comparator {
        private Map<Graph.Vertex, Integer> map;

        private Comparator(Graph graph) {
            map = indexMap(graph);
        }

        private Map<Graph.Vertex, Integer> indexMap(Graph graph) {
            Map<Graph.Vertex, Integer> map = new HashMap<>();
            int counter = 0;
            for (Graph.Vertex vertex : graph.getVertices()) {
                map.put(vertex, counter);
                counter++;
            }
            return map;
        }

        private int compare(Graph.Vertex vertex1, Graph.Vertex vertex2) {
            return map.get(vertex1).compareTo(map.get(vertex2));
        }
    }

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
