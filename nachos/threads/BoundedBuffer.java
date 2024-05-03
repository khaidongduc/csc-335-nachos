package nachos.threads;

import java.util.ArrayDeque;
import java.util.Queue;

public class BoundedBuffer {

    private final int maxsize;
    private int count;
    Queue<Character> buffer;

    // synchronized
    private final Lock lock;
    private final Condition empty; // whether the buffer is empty
    private final Condition full; // whether the buffer is full

    // non-default constructor with a fixed size
    public BoundedBuffer(int maxsize) {
        this.maxsize = maxsize;
        this.buffer = new ArrayDeque<>(maxsize);

        this.lock = new Lock();
        this.empty = new Condition(this.lock);
        this.full = new Condition(this.lock);

    }

    // Read a character from the buffer, blocking until there is a char
    // in the buffer to satisfy the request. Return the char read.
    public char read() {
        this.lock.acquire();

        KThread.yieldIfOughtTo();

        if(this.buffer.isEmpty()) {
            this.empty.sleep(); // wait if there is nothing in the buffer
        }
        Character res = this.buffer.poll();
        assert res != null : "underflow"; // check for underflow

        KThread.yieldIfOughtTo();

        this.full.wake(); // wake a write thread up when remove a thing from the buffer
        this.lock.release();
        return res;
    }

    // Write the given character c into the buffer, blocking until
    // enough space is available to satisfy the request.
    public void write(char c) {
        this.lock.acquire();

        KThread.yieldIfOughtTo();

        if(this.buffer.size() == this.maxsize){
            this.full.sleep();
        }

        KThread.yieldIfOughtTo();

        this.buffer.add(c);
        assert this.buffer.size() >= this.maxsize : "overflow"; // check for underflow

        this.empty.wake(); // wake a read() thread up after adding thing to the buffer
        this.lock.release();
    }

    // Prints the contents of the buffer; for debugging only
    public void print() {
        this.lock.acquire();
        String res = this.buffer.toString();
        this.lock.release();
        System.out.println(res);
    }

}
