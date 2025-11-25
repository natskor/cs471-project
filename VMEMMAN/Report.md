# Virtual Memory Management Problem Report
***Author: Natalie Avila***
# Summary
This project implements the page replacement algorithms: First In First Out (FIFO), Least Recently Used (LRU), Most Recently Used (MRU), and optimal. Each algorithm calculates the Page Fault Rate when given a list of page numbers and a specified number of allocated frames. 

The program runs with the provided data of the project. It will store the data (virtual addresses) and convert it to page numbers with the specified page size. The page numbers and the specified number of allocated frames will be given to each page replacement algorithm. Finally, all data will be writen to an output file, this includes the Page Size, number of frames, Page Replacement Algorithm, and Page Fault Percentage.
# Program Structure
The project consists of five source files:
* ```Main.java``` - Reads input, converts virtual addresses to page numbers, calls each algorithm, and writes formatted output to the output file.
* ```FIFO.java``` - simulates FIFO replacement algorithm.
* ```LRU.java``` - simulates LRU replacement algorithm.
* ```MRU.java``` - simulates MRU replacement algorithm.
* ```optimal.java``` - simulates optimal replacement algorithm.
## Algorithm Information
* The page replacement algorithms requires the list of page numbers and the frame count (number of allocated frames). The algorithms contain the ```frames``` array list, which simulates the number of allocated frames to the process, and a fault counter. 
* As the simulation goes through the list of page numbers, it will first check if the page number is already in memory. The following algorithms will react accordingly:
### FIFO
1) If in the array list: continue to next page number.
2) IF NOT in the array list: increment fault counter then check if ```frames``` is full. 
    * Program checking if ```frames``` is full:
        1) If NOT full, append page number. 
        2) If full, remove page number at index 0 and append new page number.
### LRU
1) If in the array list: remove that page number and append it. Then continue to next page number.
2) IF NOT in the array list: increment fault counter then check if ```frames``` is full. 
    * Program checking if ```frames``` is full:
        1) If NOT full, append page number. 
        2) If full, remove page number at index 0 and append new page number.
### MRU
1) If in the array list: remove that page number and append it. Then continue to next page number.
2) IF NOT in the array list: increment fault counter then check if ```frames``` is full. 
    * Program checking if ```frames``` is full:
        1) If NOT full, append page number. 
        2) If full, remove page number at the end of the list and append new page number.
### Optimal
1) If in the array list: continue to next page number.
2) IF NOT in the array list: increment fault counter then check if ```frames``` is full. 
    * Program checking if ```frames``` is full:
        1) If NOT full, append page number and continue to next page number. 
        2) If full, check if page numbers that are allocated to a frame is repeated:
        * Program checking if page numbers that are allocated to frames are repeated in the future:
            1) If NOT repeated: replace that page number with the new one.
            2) If all are repeated: compare each index to when page number is referenced again and replace the one furtherst away with the new page number.

* The algorithms will then return the Page Fault Percentage using the formula: ```page fault percentage = (faults / totalPages) * 100.0```
# Results
The program executed will produce the following results:
|Page Size    |#of frames   |Page Replacement ALG     |Page fault percentage|
|-------------|-------------|-------------------------|---------------------|
|512          |4            |FIFO                     |80.37                   
|512          |4            |LRU                      |80.00                   
|512          |4            |MRU                      |93.10                   
|512          |4            |optimal                  |56.63                   
|1024         |4            |FIFO                     |61.40                   
|1024         |4            |LRU                      |60.47                   
|1024         |4            |MRU                      |86.03                   
|1024         |4            |optimal                  |37.90                   
|2048         |4            |FIFO                     |26.67                   
|2048         |4            |LRU                      |26.03                   
|2048         |4            |MRU                      |73.40                   
|2048         |4            |optimal                  |13.97                   
|512          |8            |FIFO                     |61.00                   
|512          |8            |LRU                      |60.10                   
|512          |8            |MRU                      |91.50                   
|512          |8            |optimal                  |34.23                   
|1024         |8            |FIFO                     |23.60                   
|1024         |8            |LRU                      |22.80                   
|1024         |8            |MRU                      |81.03                   
|1024         |8            |optimal                  |11.27                   
|2048         |8            |FIFO                     |1.90                    
|2048         |8            |LRU                      |1.90                    
|2048         |8            |MRU                      |66.30                   
|2048         |8            |optimal                  |1.73                    
|512          |12           |FIFO                     |42.97                   
|512          |12           |LRU                      |42.07                   
|512          |12           |MRU                      |88.97                   
|512          |12           |optimal                  |21.20                   
|1024         |12           |FIFO                     |3.57                    
|1024         |12           |LRU                      |3.57                    
|1024         |12           |MRU                      |77.27                   
|1024         |12           |optimal                  |3.40                    
|2048         |12           |FIFO                     |1.83                    
|2048         |12           |LRU                      |1.83                    
|2048         |12           |MRU                      |60.40                   
|2048         |12           |optimal                  |1.63   

# Conclusion & Findings
From the ouput we can conclude that in every scenario the ```optimal``` algorithm is most effective as it had the smallest Page fault percentage. While the ```MRU``` is the least effecting, having the largest Page fault percentage. The ```FIFO``` & ```LRU``` algorithms yield very similar results as they either were identical or were off from each other by less than 1%. The results also show a decrease of page fault percentage when the page size and number of allocated frames were larger.
In conclusion, this project demonstrates how the different page replacement algorithm will react given different page sizes, and number of allocated frames.