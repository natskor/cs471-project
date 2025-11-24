# Producer Consumer Problem Report

**Author:** Caitlyn Heath

# Summary

This project implements a multithreaded solution to the Producer-Consumer problem. The program has ```p``` producers and  ```c``` consumers, each running its own thread. Producers create random sales records containing a sales date (DD/MM/YY), store ID, register number, and sale amount. Consumers read sales records from a shared circular buffer and calculate local sales statistics.

The buffer has a size ```b``` and ensures that each sales record is processed by only one consumer. Once all records are read in, each consumer merges local statistics with global statistics and prints results. The main thread prints the overall totals across all stores.

The program tracks:
*  Store-wide total sales
*  Month-wise total sales (in all stores)
*  Aggregate sales (all sales together)
*  Total time for simulation (from begin to end) 

Each producer generates its data randomly according to the instructions, and continues until 1000 records have been created. After each record, producers sleep for 5-40 millseconds.

Since the buffer is shared, it is controlled using semaphores to prevent race conditions and interleaving. The program was executed with different numbers of producers (```p = 2, 5, 10```), consumers (```c = 2, 5, 10```), and buffer sizes (```b = 3, 10```), for a total of 18 runs. 

# Program Structure

The project consists of one source file:
* ```ProducerConsumer.java``` - contains all logic including the following static inner classes:
  * ```SalesRecord``` - data struct class for a sales record
  * ```Statistics``` - class for storing/tracking store and month sale totals
  * ```CircularBuffer``` - shared buffer with semaphores (```empty```, ```full```, ```mutex```)
  * ```Producer``` - producer thread
  * ```Consumer``` - consumer thread
  * ```Main``` - controls threads, input, timings, and sentinels (for terminating threads safely)
 
## Buffer Structure

The program uses three semaphores inside the circular buffer:
| Semaphore | Initial Value   | Purpose                                                     |
| --------- | --------------- | ----------------------------------------------------------- |
| `empty`   | `b (size)`      | Tracks available empty buffer slots                         |
| `full`    | `0`             | Tracks available full slots (items ready to be consumed)    |
| `mutex`   | `1`             | Ensures mutual exclusion                                    |

## Producer Algorithm
1. `empty.acquire()` - wait for available space in buffer
2. `mutex.acquire() ` - enter critical section
3. Add record to buffer (in)
4. `mutex.release()` - exit critical section
5. `full.release()` - signal that a buffer slot is available for consumer

Each Producer generates random sales records with these ranges:
* DD: 1-30
* MM: 1-12
* YY: 16
* Store ID: 1 to p for # of producers
* Register Number: 1-6
* Sale Amount: 0.50-999.99

An `AtomicInteger` variable named `totalProduced` was used to atomically track how many items were generated in total, ensuring the program stops after producing 1000 items.

## Consumer Algorithm
1. `full.acquire()` - wait for available space in buffer
2. `mutex.acquire() ` - enter critical section
3. Remove a record from buffer (out)
4. `mutex.release()` - exit critical section
5. `empty.release()` - signal that a buffer slot is available for producer

Sentinels were used to signal to the Consumers that no more items could be produced, preventing them from blocking/taking the program hostage once all Producers finished. Each sentinel is a sales record with an invalid store ID, allowing the Consumer to recognize it and terminate safely.

# Program Execution

To run the program:

`javac *.java`

`java ProducerConsumer 2 2 3` 

The `<2> <2> <3>` values are the default values in the program, and can be changed by entering commands (see sample_commands.txt) such as these in the command line:

`java ProducerConsumer 5 5 10`

`java ProducerConsumer 5 2 3`

`java ProducerConsumer 10 10 10`

# Results

The program was run a total of 18 times to ensure each combination of `p`,`c`, and `b` was used.

Here is a results table for all 18 runs:
| Run #      | p  | c  | b  | Time (ms)   | Global Aggregate Sales            |
| ---------- | -- | -- | -- | ----------- | --------------------------------- |
| 1          | 2  | 2  | 3  | 11262.57 ms | Aggregate sales: 511365.87        |
| 2          | 2  | 5  | 3  | 11693.02 ms | Aggregate sales: 512983.71        |
| 3          | 2  | 10 | 3  | 11498.20 ms | Aggregate sales: 510887.27        |
| 4          | 5  | 2  | 3  | 4693.90 ms  | Aggregate sales: 487894.86        |
| 5          | 5  | 5  | 3  | 4604.35 ms  | Aggregate sales: 500418.01        |
| 6          | 5  | 10 | 3  | 4702.03 ms  | Aggregate sales: 507924.56        |
| 7          | 10 | 2  | 3  | 2338.00 ms  | Aggregate sales: 506769.47        |
| 8          | 10 | 5  | 3  | 2495.83 ms  | Aggregate sales: 508865.61        |
| 9          | 10 | 10 | 3  | 2513.88 ms  | Aggregate sales: 497762.96        |
| 10         | 2  | 2  | 10 | 11401.47 ms | Aggregate sales: 500397.38        |
| 11         | 2  | 5  | 10 | 11388.75 ms | Aggregate sales: 489768.34        |
| 12         | 2  | 10 | 10 | 11702.83 ms | Aggregate sales: 504882.36        |
| 13         | 5  | 2  | 10 | 4694.28 ms  | Aggregate sales: 516000.98        |
| 14         | 5  | 5  | 10 | 4880.98 ms  | Aggregate sales: 497587.03        |
| 15         | 5  | 10 | 10 | 5087.36 ms  | Aggregate sales: 503357.16        |
| 16         | 10 | 2  | 10 | 2323.54 ms  | Aggregate sales: 507552.29        |
| 17         | 10 | 5  | 10 | 2490.62 ms  | Aggregate sales: 504603.78        |
| 18         | 10 | 10 | 10 | 2592.92 ms  | Aggregate sales: 489300.30        |


# Conclusion/Findings

From this output, we can conclude that increasing the buffer size can slightly improve simulation time because it reduces the frequency of producers waiting when the buffer is full. However, the effect of buffer size is balanced by the number of consumers: adding more consumers generally has a larger impact on reducing simulation time, as they can process items in parallel more efficiently. While there is some interaction between the number of producers and buffer size, the primary factors affecting simulation time are the number of consumers and their ability to keep up with producers. The fastest runs were `7` (`p=10`,`c=2`, `b=3`) and `16` (`p=10`,`c=2`, `b=10`). This suggests that having more producers and fewer consumers can sometimes make the simulation run faster, probably because the work gets done quickly without too many threads switching back and forth.
