# Virtual Memory Management Problem
***Author: Natalie Avila***
## Compile & Run Instructions
1) Navigate to the VMEMMAN Directory
2) In the Console: Compile all .java files with the command ```javac src/*.java```
3) In the Console: Run the program with the command ```java -cp src Main```
## Included Files
Source File:
* ```Main.java``` - Contains the main function and the ones below:
    * ```readVirtualAddresses``` - holds and reads the provided input file of virtual addresses.
    * ```writeResult``` - writes the statistics of all algorithms with the provided parameters.
    * ```convertToPages``` - holds page numbers of converted virtual addresses (bytes).
    * ```main``` - calls all algorithm files and prints statistics into an output file.
* ```FIFO.java``` - contains one function:
    * ```pfr_fifo``` - implemented First In First Out algorithm. Returns Page Fault Rate.
* ```LRU.java``` - contains one function:
    * ```pfr_lru``` - implemented Least Recently Used algorithm. Returns Page Fault Rate.
* ```MRU.java``` - contains one function:
    * ```pfr_mru``` - implemented Most Recently Used algorithm. Returns Page Fault Rate.
* ```optimal.java``` - contains one function:
    * ```pfr_optimal``` - implemented optimal algorithm. Returns Page Fault Rate.
Compiled Files (.class files generated after running javac):
* ```Main.class``` 
* ```FIFO.class```
* ```LRU.class``` 
* ```MRU.class``` 
* ```optimal.class``` 
Other Project Files:
* ```README.md``` - instructions on how to compile and run the program, and details on file structure.
* ```sample_virtual_addressess.txt``` - provided input file of virtual addresses.
* ```sample_output.txt``` - sample output generated after running the program (contains statistics).
* ```report.md``` - detailed report and summary on process and findings of the program.
