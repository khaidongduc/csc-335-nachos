package nachos.userprog;

import com.sun.source.tree.Tree;
import nachos.machine.*;
import nachos.threads.*;
import nachos.userprog.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A kernel that can support multiple user processes.
 */
public class UserKernel extends ThreadedKernel {
    /**
     * Allocate a new user kernel.
     */
    public UserKernel() {
		super();
    }

    /**
     * Initialize this kernel. Creates a synchronized console and sets the
     * processor's exception handler.
     */
    public void initialize(String[] args) {
	super.initialize(args);

	console = new SynchConsole(Machine.console());
	
	Machine.processor().setExceptionHandler(new Runnable() {
		public void run() { exceptionHandler(); }
	    });

	int numPhysPages = Machine.processor().getNumPhysPages(); // number of physical frames
		// propagate this.freeFrames with {1, 2, ..., <numPhysPages>}
	freeFrames = IntStream.range(0, numPhysPages).boxed().collect(Collectors.toCollection(TreeSet::new));
	freeFrameLock = new Lock();
    }

    /**
     * Test the console device.
     */	
    public void selfTest() {
	super.selfTest();

	System.out.println("Testing the console device. Typed characters");
	System.out.println("will be echoed until q is typed.");

	char c;

	do {
	    c = (char) console.readByte(true);
	    console.writeByte(c);
	}
	while (c != 'q');

	System.out.println("");
    }

    /**
     * Returns the current process.
     *
     * @return	the current process, or <tt>null</tt> if no process is current.
     */
    public static UserProcess currentProcess() {
	if (!(KThread.currentThread() instanceof UThread))
	    return null;
	
	return ((UThread) KThread.currentThread()).process;
    }

    /**
     * The exception handler. This handler is called by the processor whenever
     * a user instruction causes a processor exception.
     *
     * <p>
     * When the exception handler is invoked, interrupts are enabled, and the
     * processor's cause register contains an integer identifying the cause of
     * the exception (see the <tt>exceptionZZZ</tt> constants in the
     * <tt>Processor</tt> class). If the exception involves a bad virtual
     * address (e.g. page fault, TLB miss, read-only, bus error, or address
     * error), the processor's BadVAddr register identifies the virtual address
     * that caused the exception.
     */
    public void exceptionHandler() {
	Lib.assertTrue(KThread.currentThread() instanceof UThread);

	UserProcess process = ((UThread) KThread.currentThread()).process;
	int cause = Machine.processor().readRegister(Processor.regCause);
	process.handleException(cause);
    }

    /**
     * Start running user programs, by creating a process and running a shell
     * program in it. The name of the shell program it must run is returned by
     * <tt>Machine.getShellProgramName()</tt>.
     *
     * @see	nachos.machine.Machine#getShellProgramName
     */
    public void run() {
	super.run();

	UserProcess process = UserProcess.newUserProcess();
	
	String shellProgram = Machine.getShellProgramName();	
	Lib.assertTrue(process.execute(shellProgram, new String[] { }));

	KThread.currentThread().finish();
    }

    /**
     * Terminate this kernel. Never returns.
     */
    public void terminate() {
	super.terminate();
    }

    /** Globally accessible reference to the synchronized console. */
    public static SynchConsole console;

    // dummy variables to make javac smarter
    private static Coff dummy1 = null;

	///////////////////////////////////////////////////////////////////////////

	private static TreeSet<Integer> freeFrames;
	private static Lock freeFrameLock;

	/**
	 * return a list of <requested> free frame numbers that can be used for a process
	 * @param requested number of free frames requested
	 * @return array of frame numbers that the process can use or null
	 * if request cannot be fulfilled */
	public static int[] allocatePages(int requested) {
		freeFrameLock.acquire();

		int numAvailableFrames = freeFrames.size();
		if(numAvailableFrames < requested) {
			freeFrameLock.release();
			return null;
		}
		int[] res = new int[requested];
		for (int i = 0 ; i < requested; ++ i) {
			Integer frame = freeFrames.pollFirst();
			assert frame != null;
			res[i] = frame;
		}

		freeFrameLock.release();
		return res;
	}

	/**
	 * put frameNumber back in the free frames list
 	 */
	public static void releasePage(int frameNumber){
		freeFrameLock.acquire();

		assert !freeFrames.contains(frameNumber);
		freeFrames.add(frameNumber);

		freeFrameLock.release();
	}

}
