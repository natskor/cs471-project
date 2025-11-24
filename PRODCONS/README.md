# Producer Consumer Problem
**Author:** Caitlyn Heath

## Compile Instructions

```javac *.java```

This command is run from the PRODCONS directory, and will compile the necessary files for running the program.

## Run Instructions

```java ProducerConsumer <p> <c> <b>```

where:
* ```p``` = number of producers
*  ```c``` = number of consumers
*  ```b``` = size of buffer.

This command is also run from the PRODCONS directory, and will run the main program with the given arguments.

### Example

```java ProducerConsumer 2 2 3```

This command runs the program with 2 producers, 2 consumers, and a buffer size of 3.

## Included Files

Source File (in src/):

* ```ProducerConsumer.java``` - Contains the full implementation, including all static inner classes:

  * ```SalesRecord``` - holds data for a sale entry

  * ```Statistics``` - tracks store, monthly, and aggregate values

  * ```CircularBuffer``` - shared buffer implemented with semaphores

  * ```Producer``` - thread that produces/generates random sales records according to instructions

  * ```Consumer``` - thread that consumes/removes records, and merges local and global stats

Compiled Files (in bin/ after running javac):

* ```ProducerConsumer.class```

* ```ProducerConsumer$SalesRecord.class```

* ```ProducerConsumer$Statistics.class```

* ```ProducerConsumer$CircularBuffer.class```

* ```ProducerConsumer$Producer.class```

* ```ProducerConsumer$Consumer.class```

Other Project Files:

* ```README.md``` - instructions on how to compile and run the program, and details on file structure

* ```sample_input.txt``` - sample input with arguments/values for number of producers, consumers, and buffer size

* ```sample_commands.txt``` - sample commands for easy access to command line runs

* ```sample_output.txt``` - sample output to show expected output of program runs

* ```report.md``` - detailed report and summary on process and findings of the program
