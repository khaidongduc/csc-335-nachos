package nachos.threads;

import nachos.machine.Lib;
import nachos.machine.Machine;

/**
 * An implementation of condition variables that disables interrupt()s for
 * synchronization.
 *
 * <p>
 * You must implement this.
 *
 * @see	nachos.threads.Condition
 */
public class Condition2 {
    /**
     * Allocate a new condition variable.
     *
     * @param	conditionLock	the lock associated with this condition
     *				variable. The current thread must hold this
     *				lock whenever it uses <tt>sleep()</tt>,
     *				<tt>wake()</tt>, or <tt>wakeAll()</tt>.
     */
    public Condition2(Lock conditionLock) {
        this.conditionLock = conditionLock;
        this.waitQueue = new SynchList();
    }

    /**
     * Atomically release the associated lock and go to sleep on this condition
     * variable until another thread wakes it using <tt>wake()</tt>. The
     * current thread must hold the associated lock. The thread will
     * automatically reacquire the lock before <tt>sleep()</tt> returns.
     */
    public void sleep() {
        Lib.assertTrue(conditionLock.isHeldByCurrentThread());

        conditionLock.release(); // release the lock
        boolean intStatus = Machine.interrupt().disable(); // disable interrupts
        waitQueue.add(KThread.currentThread()); // put current thread into the wait queue
        KThread.sleep(); // block current thread
        Machine.interrupt().restore(intStatus); // restore interrupts
        conditionLock.acquire(); //  reacquire the lock
    }

    /**
     * Wake up at most one thread sleeping on this condition variable. The
     * current thread must hold the associated lock.
     */
    public void wake() {
        Lib.assertTrue(conditionLock.isHeldByCurrentThread());
        if (!waitQueue.isEmpty()) {
            boolean intStatus = Machine.interrupt().disable(); // disable interrupts
            ((KThread) waitQueue.removeFirst()).ready(); // dispatch thread from the wait queue
            Machine.interrupt().restore(intStatus); // restore interrupts
        }
    }

    /**
     * Wake up all threads sleeping on this condition variable. The current
     * thread must hold the associated lock.
     */
    public void wakeAll() {
    	Lib.assertTrue(conditionLock.isHeldByCurrentThread());
        boolean intStatus = Machine.interrupt().disable(); // disable interrupts
        while(!waitQueue.isEmpty()) {
            ((KThread) waitQueue.removeFirst()).ready(); // dispatch thread from the wait queue
        }
        Machine.interrupt().restore(intStatus); // restore interrupts
    }

    private Lock conditionLock;

    private SynchList waitQueue;

}
