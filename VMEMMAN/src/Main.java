import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    private static final int[] pageSizes = {512, 1024, 2048};   //Page sizes in bytes
    private static final int[] numFrames = {4, 8, 12};          //Number of frames allocated

    /* Reads given file of sample virtual addresses & places and returns array */
    public static int[] readVirtualAddresses() {
        int[] addresses = new int[3000]; //creates a new array with a size of the given sample input file

        try (BufferedReader reader = new BufferedReader(new FileReader("input/sample_virtual_addresses.txt"))) {
            String line;
            int i = 0;
            while((line = reader.readLine()) != null) {
                addresses[i] = Integer.parseInt(line.trim());
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    /* Writes to Output file in output dir */
    public static void writeResult(PrintWriter writer, int pageSize, int frames, String alg, double pageFault) {
        writer.printf("%-10d %-10d %-10s %-14.2f%n", pageSize, frames, alg, pageFault);
    }

    /* Converts gvien virtual addresses (bytes) to page numbers with given page size (bytes) 
        returns array of pages */
    public static int[] convertToPages(int[] virtualAddresses, int pageSize) {
        int[] pages = new int[virtualAddresses.length];
        for( int i = 0; i < virtualAddresses.length; i++) {
            pages[i] = virtualAddresses[i] / pageSize;
        }
        return pages;
    }
/* ---------- MAIN ---------- */
    public static void main(String[] args) {
        int[] virtualAddresses = readVirtualAddresses();

        FIFO f = new FIFO(); 
        LRU l = new LRU();
        MRU m = new MRU();
        optimal o = new optimal();

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter("output/sample_output.txt"));
            writer.printf("%-10s %-10s %-22s %-20s%n", "Page Size", "#of frames", "Page Replacement ALG", "Page fault percentage");
           
            for(int frame : numFrames) {
                for(int pageSize : pageSizes) {
                    int[] pages = convertToPages(virtualAddresses, pageSize);      
                                 
                    double pfr_FIFO = f.pfr_fifo(pages, frame);
                    double pfr_LRU = l.pfr_lru(pages, frame);
                    double pfr_MRU = m.pfr_mru(pages, frame);
                    double pfr_optimal = o.pfr_opt(pages, frame);

                    writeResult(writer, pageSize, frame, "FIFO", pfr_FIFO);
                    writeResult(writer, pageSize, frame, "LRU", pfr_LRU);
                    writeResult(writer, pageSize, frame, "MRU", pfr_MRU);
                    writeResult(writer, pageSize, frame, "optimal", pfr_optimal);
                }
            }
            System.out.println("Results in output/sample_output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }
}
