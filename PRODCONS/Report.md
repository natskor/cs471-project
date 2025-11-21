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

## Consumer Algorithm
1. `full.acquire()` - wait for available space in buffer
2. `mutex.acquire() ` - enter critical section
3. Remove a record from buffer (out)
4. `mutex.release()` - exit critical section
5. `empty.release()` - signal that a buffer slot is available for producer

