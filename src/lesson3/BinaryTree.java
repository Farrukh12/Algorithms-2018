package lesson3;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// Attention: comparable supported but comparator is not
@SuppressWarnings("WeakerAccess")
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
        } else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        } else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    @Override
    public boolean remove(Object o) {
        if (root == null) return false;
        T t = (T) o;
        root = remove(root, t);
        size--;
        return true;

    }

    private Node<T> remove(Node<T> node, T t) {
        if (node == null) return null;
        int compare = t.compareTo(node.value);
        if (compare == 0){
            if (node.right == null || node.left == null) {

                if (node.right != null) return node.right;
                else return node.left;

            } else {

                Node minNode1 = new Node<>(minNode(node.right).value);
                minNode1.left = node.left;
                minNode1.right = node.right;
                node = minNode1;

                node.right = remove(node.right, node.value);
            }
        }
        if (compare > 0) {
            node.right = remove(node.right, t);
        } else if (compare < 0) {
            node.left = remove(node.left, t);
        }
        return node;
    }
    private Node<T> minNode(Node<T> node){
        Node<T> nodeMinimum = node;
        while (nodeMinimum.left != null) {
            nodeMinimum = nodeMinimum.left;
        }
        return nodeMinimum;
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
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        } else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {

        private Node<T> current = null;
        private Stack<Node<T>> stack;

        private BinaryTreeIterator() {
            stack = new Stack<>();
        }

        /**
         * Поиск следующего элемента
         * Средняя
         */
        private Node<T> findNext() {
            if (root == null || (current != null && current.value == last())) return null;
            if (current == null) {
                current = root;
                while (current.left != null) {
                    stack.push(current);
                    current = current.left;
                }
                return current;
            }
            if (current.right == null) return current = stack.pop();
            current = current.right;
            while (current.left != null){
                stack.push(current);
                current = current.left;
            }
            return current;

        }

        @Override
        public boolean hasNext() {
            return findNext() != null;
        }

        @Override
        public T next() {
            current = findNext();
            return current.value;
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        @Override
        public void remove() {
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
        SortedSet<T> treeSet = new TreeSet<>();
        headSet(root, toElement, treeSet);
        return treeSet;
    }
    private void headSet(Node<T> current, T toElement, SortedSet<T> result) {
        int compare = toElement.compareTo(current.value);
        if (current.left != null){
            headSet(current.left, toElement, result);
        }
        if (compare > 0) {
            result.add(current.value);
            if (current.right != null) {
                headSet(current.right, toElement, result);
            }
            if (current.left != null) {
                headSet(current.left, toElement, result);
            }
        } else if (compare == 0) {
            if (current.left != null) {
                addtoSet(current.left, result);
            }
        }
    }

    private void addtoSet(Node<T> node, SortedSet<T> result) {
        result.add(node.value);
        if (node.right != null) {
            addtoSet(node.right, result);
        }
        if (node.left != null) {
            addtoSet(node.left, result);
        }

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
}
