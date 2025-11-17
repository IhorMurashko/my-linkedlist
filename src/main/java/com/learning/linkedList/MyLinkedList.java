package com.learning.linkedList;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A doubly-linked list implementation that provides iteration capabilities.
 * This class implements the Iterable interface and provides methods for adding, removing,
 * and accessing elements in a linked list data structure.
 *
 * @param <E> the type of elements held in this collection
 */
public class MyLinkedList<E> implements Iterable<E> {

    private transient Node<E> first;
    private transient Node<E> last;
    private transient int modCount = 0;
    private transient int size = 0;

    /**
     * Constructs an empty list.
     */
    public MyLinkedList() {
    }

    /**
     * Returns true if this list contains no elements.
     *
     * @return true if this list contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    public int size() {
        return size;
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param e element to be appended to this list
     * @return true (as specified by Collection.add)
     */
    public boolean add(E e) {
        add(e, size);
        return true;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     *
     * @param index index at which the specified element is to be inserted
     * @param e     element to be inserted
     * @return true (as specified by Collection.add)
     * @throws IndexOutOfBoundsException if index is out of range (index < 0 || index > size())
     */
    public boolean add(int index, E e) {
        add(e, index);
        return true;
    }

    /**
     * Private helper method to add an element at specified index
     */
    private void add(E e, int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == size) {
            linkLast(e);
        } else {
            linkBefore(e, node(index));
        }
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if index is out of range (index < 0 || index >= size())
     */
    public E get(int index) {
        return node(index).item;
    }

    /**
     * Returns the Node at the specified index.
     */
    private Node<E> node(int index) {
        checkIndex(index);
        Node<E> x;
        if (index < (size >> 1)) {
            x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
        } else {
            x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
        }
        return x;
    }

    /**
     * Links e as last element.
     */
    private void linkLast(E e) {
        Node<E> l = last;
        Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
        modCount++;
    }

    /**
     * Links e before non-null Node succ.
     */
    private void linkBefore(E e, Node<E> succ) {
        Node<E> prev = succ.prev;
        Node<E> newNode = new Node<>(prev, e, succ);
        succ.prev = newNode;
        if (prev == null) {
            first = newNode;
        } else {
            prev.next = newNode;
        }
        size++;
        modCount++;
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     *
     * @param index   index of the element to replace
     * @param element element to be stored at the specified position
     * @throws IndexOutOfBoundsException if index is out of range (index < 0 || index >= size())
     */
    public void set(int index, E element) {
        checkIndex(index);
        Node<E> x = node(index);
        x.item = element;
    }

    /**
     * Checks if the given index is in range.
     */
    private void checkIndex(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * Returns an iterator over elements of type {@code E}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<E> iterator() {
        return new It();
    }

    /**
     * Unlinks non-null Node x.
     */
    private void unlink(Node<E> x) {
        Node<E> prevNode = x.prev;
        Node<E> nextNode = x.next;
        if (prevNode == null) {
            first = nextNode;
        } else {
            prevNode.next = nextNode;
            x.prev = null;
        }

        if (nextNode == null) {
            last = prevNode;
        } else {
            nextNode.prev = prevNode;
            x.next = null;
        }
        x.item = null;
        modCount++;
        size--;
    }

    /**
     * Iterator implementation for MyLinkedList
     */
    private class It implements Iterator<E> {
        private Node<E> lastReturned;
        private Node<E> next;
        private int expectedModCount = modCount;

        It() {
            next = first;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public E next() {
            checkForComodification();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = next;
            next = next.next;
            return lastReturned.item;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException("No element to remove");
            }
            checkForComodification();
            Node<E> lastNext = lastReturned.next;
            unlink(lastReturned);
            if (next == lastReturned) {
                next = lastNext;
            }
            lastReturned = null;
            expectedModCount = modCount;
        }

        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

    }

    /**
     * Node class representing elements in the linked list
     */
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
}