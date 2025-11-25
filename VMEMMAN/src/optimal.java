import java.util.ArrayList;

public class optimal {
    /* Calculates Page fault rate for optimal */
    public double pfr_opt(int[] pages, int frameCount) {
        ArrayList<Integer> frames = new ArrayList<>(frameCount);
        int faults = 0;

        for(int i = 0; i < pages.length; i++) {
            int page = pages[i];

            if(frames.contains(page)) {             //If page in list, continue to next page 
                continue;
            }

            faults++;                               //If page not in list, increment faults

            if(frames.size() < frameCount) {        //If list is not full, add to the end of the list,
                frames.add(page);                   //then continue to next page
                continue;
            }                          
   

            ArrayList<Integer> future = new ArrayList<>(); //
            for(int j = i + 1; j < pages.length; j++){     //Converts the pages array to 
                future.add(pages[j]);                      //an array list
            }                                              //

            int ejectIndex = -1;
            int farthestIndex = -1;
            for( int k = 0; k < frames.size(); k++) {     
                int currentFramePage = frames.get(k);

                if (!future.contains(currentFramePage)) { //If the current frame page is not repeated
                    ejectIndex = k;                       //exit the loop so it may be replaced
                    break;
                }

                int futureIndex = future.indexOf(currentFramePage); //Find future index when page in frame is repeated
                if (futureIndex > farthestIndex) {
                    farthestIndex = futureIndex;                    
                    ejectIndex = k;                                 //The furthest Index repeated will be replaced
                }
            }
            frames.set(ejectIndex, page);
        }
        return 100.0 * faults / pages.length;
    }
}
