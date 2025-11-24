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

The <2> <2> <3> values are the default values in the program, and can be changed by entering commands such as these in the command line:

`java ProducerConsumer 5 5 10`

`java ProducerConsumer 5 2 3`

`java ProducerConsumer 10 10 10`

# Results

The program was run a total of 18 times to ensure each combination of `p`,`c`, and `b` was used.

Here is a complete results table for all 18 runs:
| Run        | p  | c  | b  | Time (ms)   | Global Aggregate Sales            |
| ---------- | -- | -- | -- | ----------- | --------------------------------- |
| 1          | 2  | 2  | 3  | 11262.57 ms | Aggregate sales: 511365.87        |
| 2          | 2  | 5  | 3  | 11693.02 ms | Aggregate sales: 512983.71        |
| 3          | 2  | 10 | 3  | 11498.20 ms | Aggregate sales: 510887.27        |
| 4          | 5  | 2  | 3  | 4693.90 ms  |                                   |
| 5          | 5  | 5  | 3  | ~ ms      |            |
| 6          | 5  | 10 | 3  | ~ ms      |  |
| 7          | 10 | 2  | 3  | ~ ms      |           |
| 8          | 10 | 5  | 3  | ~ ms      |          |
| 9          | 10 | 10 | 3  | ~ ms      |           |
| 10         | 2  | 2  | 10 | ~ ms      |  |
| 11         | 2  | 5  | 10 | ~ ms      |            |
| 12         | 2  | 10 | 10 | ~ ms      |           |
| 13         | 5  | 2  | 10 | ~ ms      |              |
| 14         | 5  | 5  | 10 | ~ ms      |  |
| 15         | 5  | 10 | 10 | ~ ms      |           |
| 16         | 10 | 2  | 10 | ~ ms      |         |
| 17         | 10 | 5  | 10 | ~ ms      |           |
| 18         | 10 | 10 | 10 | ~ ms      |           |


# Conclusion/Findings