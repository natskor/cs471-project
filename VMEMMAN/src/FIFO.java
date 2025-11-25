import java.util.ArrayList;

public class FIFO {  
    /* Calculates the Page fault rate for FIFO */
    public double pfr_fifo(int[] pages, int frameCount) {
        ArrayList<Integer> frames = new ArrayList<>(frameCount);
        int faults = 0;

        for(int page : pages) {
            if(frames.contains(page)) {         //If page is already in list, continue to next page
                continue;
            }
            faults++;                           //If not in list, increment faults

            if(frames.size() < frameCount) {    //If list is not full, add to the end of the list
                frames.add(page);
            } else{                             //If list is full, remove index 0
                frames.remove(0);        //then add new page at the end of the list
                frames.add(page);
            }
        }
        return 100.0 * faults / pages.length;
    }
}
