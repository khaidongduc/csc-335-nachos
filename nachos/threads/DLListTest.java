package nachos.threads;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DLListTest {

    @org.junit.jupiter.api.Test
    void removeHead() {
        int[] arr = {3, 5, 1, 4, 2, 6};
        DLList dlList = new DLList();
        for(int i : arr){
            dlList.insert(Integer.toString(i), i);
        }
        String head = (String) dlList.removeHead();
        assertEquals("1", head);
        assertEquals("([2,2] [3,3] [4,4] [5,5] [6,6])", dlList.toString());
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
            dlList.insert(Integer.toString(i), i);
        }

        assertEquals("([1,1] [2,2] [3,3] [4,4] [5,5] [6,6])", dlList.toString());
        assertEquals("([6,6] [5,5] [4,4] [3,3] [2,2] [1,1])", dlList.reverseToString());
        assertEquals(arr.length, dlList.size());
    }

}