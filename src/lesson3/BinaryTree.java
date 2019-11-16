package lesson3;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// Attention: comparable supported but comparator is not
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;

        Node<T> left = null;

        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        }
        else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        }
        else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    public int height() {
        return height(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    @Override
    public boolean remove(Object o) {
        if (find((T) o) == null) return false;  //  Делаем проверку на вхождение элемента в дерево
        root = remove(root, new Node<>((T) o));
        size--;  //  Уменьшаем количество элементов при успешном удалении
        return true;
    }  //  Вывод: Т=O(h), R=O(1), где h - высота бинарного дерева

    /**
     * Вспомогательная функция
     * Удаление элемента в поддереве
     * Реализовано рекурсией
     *
     * Рассматривается 3 случая:
     * 1) Удаляемый элемент находится в левом поддереве текущего поддерева
     * 2) Удаляемый элемент находится в правом поддереве текущего поддерева
     * 3) Удаляемый элемент является корнем поддерева
     *
     * В двух первых случаях рекурсивно удаляется элемент из нужного поддерева.
     * Если удаляемый элемент является корнем текущего поддерева и имеет два узла,
     * то нужно заменить его минимальным элементом из правого поддерева и рекурсивно
     * удалить этот минимальный элемент из правого поддерева.
     * Если удаляемый элемент имеет один узел, то он им и заменяется.
     */
    private Node<T> remove(Node<T> start, Node<T> removable) {
        if (start == null) return null;
        if (removable.value.compareTo(start.value) < 0) start.left = remove(start.left, removable);  //  Удаление слева
        else if (removable.value.compareTo(start.value) > 0) start.right = remove(start.right, removable);  //  Удаление справа
        else if (start.left != null && start.right != null) {  //  Удаление элемента при двух узлах с непустыми значениями
            Node<T> node;  //  Используется переменная узла, так как value является final
            node = new Node<>(minimum(start.right));  //  Поиск минимального элемента в правом поддереве (вспом. функ.)
            node.left = start.left;
            node.right = start.right;
            start = node;
            start.right = remove(start.right, start);
        } else {
            if (start.left != null) start = start.left;
            else start = start.right;
        }
        return start;
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {

        private Node<T> node = null;  //  Текущий элемент в итераторе
        private Node<T> previous = null;  //  Предыдущий элемент в итераторе
        private Stack<Node<T>> stackOfParents = new Stack<>();  //  Стэк хранящий всех родителей для текущего элемента

        private BinaryTreeIterator() {
            if (root == null) return;
            node = root;
            stackOfParents.push(null);  //  Добавляем в начало стэка null, чтобы при окончании итерации node был равен null
            while (node.left != null) {  //  Заполнение стэка родителями для первого элемента
                stackOfParents.push(node);
                node = node.left;
            }
        }

        /**
         * Проверка наличия следующего элемента
         * Средняя
         */
        @Override
        public boolean hasNext() {
            return node != null;
        }  //  Вывод: Т=O(1), R=O(1)

        /**
         * Поиск следующего элемента
         * Средняя
         */
        @Override
        public T next() {
            previous = node;
            node = findNext(node);
            return previous.value;
        }  //  Вывод: Т=O(h), R=O(1), где h - высота бинарного дерева

        /**
         * Удаление следующего элемента
         * Сложная
         */
        @Override
        public void remove() {
            BinaryTree.this.remove(previous.value);  //  Удаление элемента в самом бинарном дереве
            while (!stackOfParents.empty()) {  //  Опусташаем стэк, для его переопределения для нового текущего элемента
                stackOfParents.pop();
            }
            stackOfParents.push(null);
            if (node == null) return;  //  Если элемент является последним, то есть null, то метод завершается, чтобы
            node = findWithParents(root, node.value);  //  не искать родителей для null(иначе вылетает экспешн)
        }  //  Вывод: Т=O(h), R=O(1), где h - высота бинарного дерева

        /**
         * Вспомогательная функция
         * Поиск следующего элемента в дереве с заполнением стэка с родителями
         *
         * Алгоритм:
         * Рассматривается два случая:
         * 1) Следующий элемент находится в правом поддереве относительно текущего элемента
         * 2) Следующий элемент находится выше в дереве
         *
         * Для первого случая смотрим, есть ли у текущего элемента справа потомок.
         * Если есть, то добавляем текущий элемент в стэк и запускаем поиск минимального
         * элемента с добавлением родителей.
         * Для второго случая создаём переменную, хранящую ячейку родителя, и, пока элемент справа
         * не перестанет быть равным текущему элементу, происходит переприсваивание переменных:
         * Текущему элементу присваивается его родитель, а родителю элемент выше(тоже родитель).
         */
        private Node<T> findNext(Node<T> node) {
            if (node.right != null) {
                stackOfParents.push(node);
                return minimumWithParents(node.right);
            }
            Node<T> parent = stackOfParents.pop();
            while (parent != null && node == parent.right) {
                node = parent;
                parent = stackOfParents.pop();
            }
            return parent;
        }

        /**
         * Вспомогательная функция
         * Поиск элемента с заполнением стэка родителями
         * Реализовано рекурсией
         * Тот же самый find(), только ещё добавляет родителей для искомого элемента в стэк.
         */
        private Node<T> findWithParents(Node<T> start, T value) {
            int comparison = value.compareTo(start.value);
            if (comparison == 0) {
                return start;
            }
            else if (comparison < 0) {
                stackOfParents.push(start);
                if (start.left == null) return start;
                return findWithParents(start.left, value);
            }
            else {
                stackOfParents.push(start);
                if (start.right == null) return start;
                return findWithParents(start.right, value);
            }
        }

        /**
         * Вспомогательная функция
         * Поиск минимального элемента с заполнением стэка родителями
         * Является тем же самым minimum(), только с добавлением родителей в стэк.
         */
        private Node<T> minimumWithParents(Node<T> node) {
            if (node == null) throw new NoSuchElementException();
            while (node.left != null) {
                stackOfParents.push(node);
                node = node.left;
            }
            return node;
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }

    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return new SubBinaryTree(this, fromElement, toElement);
    }  //  Вывод: Т=O(e), R=O(1), где e - количество элементов в дереве

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        return new SubBinaryTree(this, null, toElement);
    }  //  Вывод: Т=O(e), R=O(1), где e - количество элементов в дереве

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return new SubBinaryTree(this, fromElement, null);
    }  //  Вывод: Т=O(e), R=O(1), где e - количество элементов в дереве

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    /**
     * Вспомогательная функция
     * Нахождение минимального элемента в поддереве
     */
    public T minimum(Node<T> node) {
        if (node == null) throw new NoSuchElementException();
        Node<T> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    /**
     * Вспомогательная функция (нигде не используется)
     * Нахождение максимального элемента в поддереве
     */
    public T maximum(Node<T> node) {
        if (node == null) throw new NoSuchElementException();
        Node<T> current = node;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    /**
     * Вложенный класс, реализующий поддерево, которое ограничено либо снизу,
     * либо сверху, либо с двух сторон одновременно.
     * Является классом-обёрткой, так как мы работаем с тем же самым бинарным деревом,
     * а не новым его экземпляром.
     *
     * Переменные:
     * binaryTree - бинарное дерево, с которым мы работаем
     * fromElement - нижняя граница, которая ограничивает поддерево (элемент включается в дерево)
     * toElement - верхняя граница, которая ограничивает поддерево (элемент не включается в дерево)
     *
     * Переопределены методы add(), contains() и size() из родительского класса.
     */
    class SubBinaryTree extends BinaryTree<T> {
        private BinaryTree<T> binaryTree;
        private final T fromElement;
        private final T toElement;
        private int size = 0;

        SubBinaryTree(BinaryTree<T> binaryTree, T fromElement, T toElement) {
            this.binaryTree = binaryTree;
            this.fromElement = fromElement;
            this.toElement = toElement;
        }

        @Override
        public boolean add(T t) {
            if (!inRange(t)) throw new IllegalArgumentException();
            return binaryTree.add(t);
        }

        @Override
        public boolean contains(Object o) {
            if (!inRange((T) o)) return false;
            return binaryTree.contains(o);
        }

        @Override
        public boolean remove(Object o) {
            if (!inRange((T) o)) return false;
            return binaryTree.remove(o);
        }

        @Override
        public int size() {
            int counter = 0;
            for (T value : binaryTree) if (inRange(value)) counter++;
            size = counter;
            return size;
        }

        /**
         * Вспомогательная функция
         * Проверка на вхождение в поддерево
         * Реализовано при помощи проверки границ на null
         *
         * Алгоритм:
         * Задаём две переменные comparisonBottom и comparisonTop,
         * которые отвечают за сравнение по нижней границе и по верхней,
         * присваиваем им 0 и -1 соответственно для того,
         * чтобы при false в проверка на null не учитывалась граница поддерева,
         * если нам это требуется (методы headSet() и tailSet()).
         * Далее присваиваем значение сравнения через compareTo между элементом и границей
         * в comparisonBottom и comparisonTop соответственно,
         * если проверка на null равна true.
         */
        private boolean inRange(T t) {
            int comparisonBottom = 0;
            int comparisonTop = -1;
            if (fromElement != null) comparisonBottom = t.compareTo(fromElement);
            if (toElement != null) comparisonTop = t.compareTo(toElement);
            return comparisonBottom >= 0 && comparisonTop < 0;
        }
    }
}