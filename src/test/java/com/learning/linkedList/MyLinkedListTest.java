package com.learning.linkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MyLinkedListTest {
    private MyLinkedList<String> list;
    private final String modCountFieldName = "modCount";

    @BeforeEach
    void setUp() {
        list = new MyLinkedList<>();
    }

    @Nested
    @DisplayName("basicIterationsTests")
    class BasicIterationsTests {
        @Test
        void testEmptyListIteration() {
            Iterator<String> it = list.iterator();

            assertFalse(it.hasNext());
            assertThrows(NoSuchElementException.class, it::next);
        }

        @Test
        void testSingleElementIteration() {
            String element = "A";
            list.add(element);
            Iterator<String> it = list.iterator();

            assertTrue(it.hasNext());
            assertEquals(element, it.next());
            assertFalse(it.hasNext());
            assertThrows(NoSuchElementException.class, it::next);
        }

        @Test
        void testMultipleElementsIteration() {
            String element1 = "A";
            String element2 = "B";
            String element3 = "C";
            String element4 = "D";

            list.add(element1);
            list.add(element2);
            list.add(element3);
            list.add(element4);

            Iterator<String> it = list.iterator();

            assertTrue(it.hasNext());
            assertEquals(element1, it.next());
            assertTrue(it.hasNext());
            assertEquals(element2, it.next());
            assertTrue(it.hasNext());
            assertEquals(element3, it.next());
            assertTrue(it.hasNext());
            assertEquals(element4, it.next());
            assertFalse(it.hasNext());
            assertThrows(NoSuchElementException.class, it::next);
        }

        @Test
        void testHasNextDoesNotMoveCursor() {
            String element1 = "A";
            String element2 = "B";
            String element3 = "C";
            String element4 = "D";

            list.add(element1);
            list.add(element2);
            list.add(element3);
            list.add(element4);

            Iterator<String> it = list.iterator();

            assertTrue(it.hasNext());
            assertTrue(it.hasNext());
            assertTrue(it.hasNext());
            assertEquals(element1, it.next());
        }

        @Test
        void testNextAfterEnd() {
            String element1 = "A";
            list.add(element1);
            Iterator<String> it = list.iterator();

            assertEquals(element1, it.next());
            assertFalse(it.hasNext());
            assertThrows(NoSuchElementException.class, it::next);
        }

        @Test
        void testMultipleNextAfterEnd() {
            String element = "A";
            list.add(element);
            Iterator<String> it = list.iterator();

            assertEquals(element, it.next());
            assertFalse(it.hasNext());
            assertThrows(NoSuchElementException.class, it::next);
            assertThrows(NoSuchElementException.class, it::next);
            assertThrows(NoSuchElementException.class, it::next);
        }
    }

    @Nested
    @DisplayName("listSizeTests")
    class ListSizeAndModCountTest {
        @Test
        void testEmptyListSizeAfterCreating() throws NoSuchFieldException, IllegalAccessException {
            Integer modCount = (Integer) getFieldValue(list, modCountFieldName);

            assertTrue(list.isEmpty());
            assertEquals(0, list.size());
            assertEquals(0, modCount);
        }

        @Test
        void testSizeIsOneAfterAddedOneElement() throws NoSuchFieldException, IllegalAccessException {
            list.add("A");
            Integer modCount = (Integer) getFieldValue(list, modCountFieldName);

            assertEquals(1, list.size());
            assertEquals(1, modCount);
            assertFalse(list.isEmpty());
        }

        @Test
        void testSizeAfterAddingMultipleElements() throws NoSuchFieldException, IllegalAccessException {
            list.add("A");
            list.add("B");
            list.add("C");
            Integer modCount = (Integer) getFieldValue(list, modCountFieldName);

            assertEquals(3, list.size());
            assertEquals(3, modCount);
            assertFalse(list.isEmpty());
        }

        @Test
        void testSizeAfterAddingElementInMiddle() throws NoSuchFieldException, IllegalAccessException {
            list.add("A");
            list.add("B");
            list.add("C");
            list.add(2, "D");
            Integer modCount = (Integer) getFieldValue(list, modCountFieldName);

            assertEquals(4, list.size());
            assertEquals(4, modCount);
            assertFalse(list.isEmpty());
        }

        @Test
        void testSizeAfterSetElementInMiddle() throws NoSuchFieldException, IllegalAccessException {
            list.add("A");
            list.add("B");
            list.add("C");
            list.set(2, "D");
            Integer modCount = (Integer) getFieldValue(list, modCountFieldName);

            assertEquals(3, list.size());
            assertEquals(3, modCount);
            assertFalse(list.isEmpty());
        }

        @Test
        void testSizeAfterSetElementInHead() throws NoSuchFieldException, IllegalAccessException {
            list.add("A");
            list.add("B");
            list.add("C");
            list.set(0, "D");
            Integer modCount = (Integer) getFieldValue(list, modCountFieldName);

            assertEquals(3, list.size());
            assertEquals(3, modCount);
            assertFalse(list.isEmpty());
        }

        @Test
        void testSizeAfterSetElementInTail() throws NoSuchFieldException, IllegalAccessException {
            list.add("A");
            list.add("B");
            list.add("C");
            list.set(list.size() - 1, "D");
            Integer modCount = (Integer) getFieldValue(list, modCountFieldName);

            assertEquals(3, list.size());
            assertEquals(3, modCount);
            assertFalse(list.isEmpty());
        }

        @Test
        void testSizeAfterRemovingAllElements() throws NoSuchFieldException, IllegalAccessException {
            list.add("A");
            list.add("B");
            list.add("C");
            Iterator<String> it = list.iterator();

            while (it.hasNext()) {
                it.next();
                it.remove();
            }

            Integer modCount = (Integer) getFieldValue(list, modCountFieldName);
            assertTrue(list.isEmpty());
            assertEquals(0, list.size());
            assertEquals(6, modCount);
        }

        @Test
        void testSizeAfterRemovingOneElement() throws NoSuchFieldException, IllegalAccessException {
            list.add("A");
            list.add("B");
            list.add("C");
            Iterator<String> it = list.iterator();

            it.next();
            it.next();
            it.remove();

            Integer modCount = (Integer) getFieldValue(list, modCountFieldName);
            assertEquals(2, list.size());
            assertEquals("A", list.get(0));
            assertEquals("C", list.get(1));
            assertFalse(list.isEmpty());
            assertEquals(4, modCount);
        }

    }

    private Object getFieldValue(MyLinkedList<?> list, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = list.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(list);
    }
}