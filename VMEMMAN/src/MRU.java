import java.util.ArrayList;

public class MRU {
    /* Calculates Page fault rate for MRU */
    public double pfr_mru(int[] pages, int frameCount) {
        ArrayList<Integer> frames = new ArrayList<>(frameCount);
        int faults = 0;

        for(int page : pages) {
            if(frames.contains(page)) {             //If page in list, add page to the end of the list, 
                frames.remove((Integer) page);      //then continue to next page
                frames.add(page);
                continue;
            }
            faults++;                               //If page not in list, increment faults

            if(frames.size() < frameCount) {        //If list is not full, add to the end of the list
                frames.add(page);
            } else{                                 //If list is full, remove index at the end (MRU)
                frames.remove(frames.size() - 1);   //then add new page at the end of the list
                frames.add(page);
            }
        }
        return 100.0 * faults / pages.length;
    }
}
