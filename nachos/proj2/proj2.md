# Project 2

Name: Khai Dong

### Part 1: Repair Project 1

The original problem is with mutual exclusion where the `DLList` breaks down upon doing concurrency.
To solve this problem, we wrap each of the method in a lock to convert the original data structure into a monitor.
```
<ret-type> <method-name>(self, <arugments>){
    lock.acquire();
    ...
    lock.release();
}
```

To allow `DLList.removeHead()` to wait until the `DLList` is not empty, I added a condition variable `empty`.

```java
public Object removeHead() {
    lock.acquire();
    while(this.first == null) {
        this.empty.sleep();
    }
    ...
}
```

Then, `DLList.insert()` will signal when an object is added to the list. This is the only method in `DLList` that insert an object
into the list (`DLList.prepend()` calls `DLList.insert()`).

```java
public void insert(Object item, Integer sortKey) {
    if(!lock.isHeldByCurrentThread())
        this.lock.acquire();

    ++ this.size; // increment size counter
    DLLElement newElem = new DLLElement(item, sortKey);
    if(this.first == null){
        this.first = this.last = newElem;
        this.empty.wake();
        this.lock.release();
        return;
    }
    
    ...
    
    this.empty.wake();
    this.lock.release();
}
```

`DLList.insert()` check if the current thread already acquire the lock before try to acquire the lock since it may be called by `DLList.prepend()`. Return to
the 2 tests from project 1, there are no longer a `NullExceptionPointer` nor an invalid input.
```agsl
([1,1] [3,3] [4,4] [6,6] (previously, ([1,1] [4, 4] [3,3] [6,6])
([1,1]) (previously throw a NullPointerException)
```

### Part 2: Bounder Buffer

### Part 3: `Condition2` Implementation

