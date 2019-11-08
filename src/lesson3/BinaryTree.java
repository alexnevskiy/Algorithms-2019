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
        private Queue<Node<T>> iterator = new LinkedList<>();  //  Очередь в качестве коллекции для реализации итератора

        private BinaryTreeIterator() {
            if (root == null) return;
            createIterator(root);
        }

        /**
         * Вспомогательная функция
         * Заполнение итератора для бинарного дерева в порядке увеличения значения
         * Реализовано рекурсией
         *
         * Алгоритм:
         * Ищем минимальное значение начиная с корня бинарного дерева рекурсией,
         * то есть проверяем на null каждый левый элемент. Как только самый малый элемент найден,
         * добавляем его в конец очереди. Далее начинается заполнение всеми последующими элементами.
         * Проверяем правый элемент на null, так как мы находимся в самой левой ячейке левого поддерева,
         * то цикл завершается и возвращается в предыдущий вызов, добавляется элемент в очередь
         * и совершается проверка опять же на null справа, если справа есть элемент, то выполняется метод,
         * добавляется элемент в очередь, в зависимости от правого элемента либо вызывается метод снова,
         * либо возвращается в прошлый, и, таким образом, обходится всё дерево.
         */
        private void createIterator(Node<T> current) {
            if (current.left != null) createIterator(current.left);
            iterator.offer(current);
            if (current.right != null) createIterator(current.right);
        }  //  Вывод: Т=O(e), R=O(e), где e - количество элементов в дереве

        /**
         * Проверка наличия следующего элемента
         * Средняя
         */
        @Override
        public boolean hasNext() {
            return iterator.peek() != null;
        }  //  Вывод: Т=O(e), R=O(1), где e - количество элементов в дереве

        /**
         * Поиск следующего элемента
         * Средняя
         */
        @Override
        public T next() {
            node = iterator.poll();
            if (node == null) throw new NoSuchElementException();
            return node.value;
        }  //  Вывод: Т=O(e), R=O(1), где e - количество элементов в дереве

        /**
         * Удаление следующего элемента
         * Сложная
         */
        @Override
        public void remove() {
            BinaryTree.this.remove(node.value);
        }  //  Вывод: Т=O(h), R=O(1), где h - высота бинарного дерева
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
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        // TODO
        throw new NotImplementedError();
    }

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
}
