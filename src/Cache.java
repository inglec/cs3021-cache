// Address shift = 2 left
// AddressRecord mask = 23

import com.sun.deploy.trace.Trace;

import java.util.Iterator;

public class Cache {
    public static final int ADDRESS_LENGTH = 25;

    private final int lineLength;  // L bytes per cache line.
    private final int directories; // K cache lines per set.
    private final int sets;        // N sets.

    private final int offsetLength, offsetMask;
    private final int setLength, setMask;
    private final int tagLength, tagMask;

    /**
     * Creates a new cache.
     * @param l:
     * @param k: If k=1, direct mapped cache. AddressRecord tag compared with ONE cache tag. Any of K cache lines.
     * @param n: If n=1, fully associative cache. AddressRecord tag compared with ALL cache tags. One cache line.
     */
    public Cache(int l, int k, int n) {
        lineLength  = l;
        directories = k;
        sets        = n;

        // Set values for address masking.
        offsetLength = (int) (Math.log(lineLength) / Math.log(2));
        setLength    = (int) (Math.log(sets) / Math.log(2));
        tagLength    = ADDRESS_LENGTH - (offsetLength + setLength);

        offsetMask = createMask(offsetLength);
        setMask    = createMask(setLength);
        tagMask    = createMask(tagLength);
    }

    private int getOffset(int address) {
        return address & offsetMask;
    }

    private int getSet(int address) {
        return (address >> offsetLength) & setMask;
    }

    private int getTag(int address) {
        return (address >> (setLength + offsetLength)) & tagMask;
    }

    public static int createMask(int ones) {
        int mask = 0;

        for (int i = 0; i < ones; i++) {
            mask <<= 1; // Shift left
            mask |= 1;  // Add 1 to end
        }

        return mask;
    }



    public static void main(String[] args) {
        TraceFile traceFile = new TraceFile("input/gcc1.trace");
        Iterator<AddressRecord> addressRecords = traceFile.getAddressRecords();

        /*for (int i = 0; addressRecords.hasNext(); i++) {
            System.out.println(i + ": " + addressRecords.next().address);
        }*/

        Cache cache_16KB_DirectMapped = new Cache(16, 1, 1024);
        // Cache cache_32KB_8Way         = new Cache(16, 8, 256);


    }
}
