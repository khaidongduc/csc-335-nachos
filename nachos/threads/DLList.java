package nachos.threads;  // don't change this. Gradescope needs it.

/**
 * An implementation of a doubly linked list
 * Edited by Khai Dong (dongk@union.edu)
 */
public class DLList
{
    private DLLElement first;  // pointer to first node
    private DLLElement last;   // pointer to last node
    private int size;          // number of nodes in list

    /**
     * Creates an empty sorted doubly-linked list.
     */
    public DLList() {
        this.first = this.last = null;
        this.size =0;
    }

    /**
     * Add item to the head of the list, setting the key for the new
     * head element to min_key - 1, where min_key is the smallest key
     * in the list (which should be located in the first node).
     * If no nodes exist yet, the key will be 0.
     */
    public void prepend(Object item) {
        int key = (this.first == null) ? 0 : this.first.key - 1;
        this.insert(item, key);
    }

    /**
     * Removes the head of the list and returns the data item stored in
     * it.  Returns null if no nodes exist.
     *
     * @return the data stored at the head of the list or null if list empty
     */
    public Object removeHead() {
        if(this.first == null) {
            return null;
        }
        Object returnData = this.first.data;
        -- this.size; // decrement the size
        this.first = this.first.next; // update new head
        if(this.first == null) { // if pop the last element
            this.last = null;
        } else {
            this.first.prev = null; // update previous of the current head
        }
        return returnData;
    }

    /**
     * Tests whether the list is empty.
     *
     * @return true iff the list is empty.
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * returns number of items in list
     * @return
     */
    public int size(){
        return this.size;
    }


    /**
     * Inserts item into the list in sorted order according to sortKey.
     */
    public void insert(Object item, Integer sortKey) {
        ++ this.size; // increment size counter
        DLLElement newElem = new DLLElement(item, sortKey);
        if(this.first == null){
            this.first = this.last = newElem;
            return;
        }
        DLLElement curElem = this.first;
        // traverse the list to find the correct position to put item
        while(curElem != null && curElem.key <= sortKey) curElem = curElem.next;
        if(curElem == null){
            this.last.next = newElem;
            newElem.prev = this.last;
            this.last = newElem;
        } else {
            newElem.prev = curElem.prev;
            newElem.next = curElem;
            curElem.prev = newElem;
            if (newElem.prev == null) this.first = newElem;
            else newElem.prev.next = newElem;
        }
    }


    /**
     * returns list as a printable string. A single space should separate each list item,
     * and the entire list should be enclosed in parentheses. Empty list should return "()"
     * @return list elements in order
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        DLLElement curNode = this.first;
        while(curNode != null){
            builder.append(curNode.toString());
            if(curNode != this.last) builder.append(' ');
            curNode = curNode.next;
        }
        builder.append(')');
        return builder.toString();
    }

    /**
     * returns list as a printable string, from the last node to the first.
     * String should be formatted just like in toString.
     * @return list elements in backwards order
     */
    public String reverseToString(){
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        DLLElement curNode = this.last;
        while(curNode != null){
            builder.append(curNode.toString());
            if(curNode != this.first) builder.append(' ');
            curNode = curNode.prev;
        }
        builder.append(')');
        return builder.toString();
    }

    /**
     *  inner class for the node
     */
    private class DLLElement
    {
        private DLLElement next;
        private DLLElement prev;
        private int key;
        private Object data;

        /**
         * Node constructor
         * @param item data item to store
         * @param sortKey unique integer ID
         */
        public DLLElement(Object item, int sortKey)
        {
            key = sortKey;
            data = item;
            next = null;
            prev = null;
        }

        /**
         * returns node contents as a printable string
         * @return string of form [<key>,<data>] such as [3,"ham"]
         */
        public String toString(){
            return "[" + key + "," + data + "]";
        }
    }


    public static class DLListTest implements Runnable {

        // shared doubly linked list
        public static DLList testList = new DLList();

        private final String label;
        private final int from, to, step;

        DLListTest(String label, int from, int to, int step){
            assert from >= to;
            assert step > 0; // make sure the func will end
            assert label != null && label.length() != 0; // make sure label is not empty or null

            this.label = label;
            this.from = from;
            this.to = to;
            this.step = step;
        }

        /**
         * Prepends multiple nodes to a shared doubly-linked list. For each
         * integer in the range from...to (inclusive), make a string
         * concatenating label with the integer, and prepend a new node
         * containing that data (that's data, not key). For example,
         * countDown("A",8,6,1) means prepend three nodes with the data
         * "A8", "A7", and "A6" respectively. countDown("X",10,2,3) will
         * also prepend three nodes with "X10", "X7", and "X4".
         *
         * This method should conditionally yield after each node is inserted.
         * Print the list at the very end.
         *
         * Preconditions: from>=to and step>0
         *
         * @param label string that node data should start with
         * @param from integer to start at
         * @param to integer to end at
         * @param step subtract this from the current integer to get to the next integer
         */
        public void countDown(String label, int from, int to, int step) {
            for (int i = from ; i >= to ; i -= step){
                String nodeLabel = label + i;
                testList.prepend(nodeLabel);
                KThread.yieldIfOughtTo();
            }
        }

        public static void reset(){
            testList = new DLList();
        }

        @Override
        public void run() {
            countDown(label, from, to, step);
            KThread.yield();
        }
    }

}