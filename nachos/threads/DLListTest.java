package nachos.threads;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class DLListTest {

    @org.junit.jupiter.api.Test
    void removeHead() {
        int[] arr = {3, 5, 1, 4, 2, 6};
        DLList dlList = new DLList();
        for(int i : arr){
            dlList.insert(i, i);
        }
        Integer head = (Integer) dlList.removeHead();
        assertEquals(1, head);
        assertEquals("(2 3 4 5 6)", dlList.toString());
        assertEquals(5, dlList.size());
    }

    @org.junit.jupiter.api.Test
    void removeHeadEmpty() {
        DLList dlList = new DLList();

        Integer head = (Integer) dlList.removeHead();
        assertNull(head);
        assertEquals("()", dlList.toString());
        assertEquals(0, dlList.size());
    }

    @org.junit.jupiter.api.Test
    void insert() {
        int[] arr = {3, 5, 1, 4, 2, 6};
        DLList dlList = new DLList();
        for(int i : arr){
            dlList.insert(i, i);
        }
        System.out.print(dlList.toString());
        System.out.print(dlList.reverseToString());

        assertEquals("(1 2 3 4 5 6)", dlList.toString());
        assertEquals("(6 5 4 3 2 1)", dlList.reverseToString());
        assertEquals(arr.length, dlList.size());
    }

}