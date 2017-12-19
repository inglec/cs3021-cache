public class Cache {
    public static final int ADDRESS_LENGTH = 25;

    private Set[] cache; // Cache is represented as an array of sets.

    private final int bytesPerLine; // L bytes per cache line.
    private final int directories;  // K cache lines per set.
    private final int sets;         // N sets.

    private int hits = 0, misses = 0; // Total cache hits and misses.

    // Values used for calculating cache location from addresses.
    //  *******************************************************
    //  *                TAG                *  SET  *  OFFSET *
    //  *******************************************************
    private final int offsetLength, offsetMask;
    private final int setLength, setMask;
    private final int tagLength, tagMask;

    /**
     * Creates a new cache.
     * @param l: The number of bytes per cache line.
     * @param k: The number of directories per set.
     *           If k=1, direct mapped cache. AddressRecord tag compared with ONE cache tag. Any of K cache lines.
     * @param n: The number of sets in the cache.
     *           If n=1, fully associative cache. AddressRecord tag compared with ALL cache tags. One cache line.
     */
    public Cache(int l, int k, int n) {
        bytesPerLine = l;
        directories  = k;
        sets         = n;

        cache = new Set[sets];
        for (int i = 0; i < sets; i++) {
            cache[i] = new Set(directories, bytesPerLine);
        }

        // Set values for address masking.
        offsetLength = (int) (Math.log(bytesPerLine) / Math.log(2));
        setLength    = (int) (Math.log(sets) / Math.log(2));
        tagLength    = ADDRESS_LENGTH - (offsetLength + setLength);

        offsetMask = createMask(offsetLength);
        setMask    = createMask(setLength);
        tagMask    = createMask(tagLength);
    }

    /**
     * Creates a binary mask with the number of ones specified.
     */
    public static int createMask(int ones) {
        int mask = 0;

        for (int i = 0; i < ones; i++) {
            mask <<= 1; // Shift left
            mask |= 1;  // Add 1 to end
        }

        return mask;
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

    public void hit(int address, int burstCount) {
        int set    = getSet(address);
        int tag    = getTag(address);
        int offset = getOffset(address);

        for (int i = 0; i < burstCount; i++) {
            if (cache[set].containsTag(tag)) {
                hits++;
                byte data = cache[set].getData(tag, offset); // Get data from cache.
            }
            else {
                misses++;
                cache[set].update(tag); // Data not in cache. Update least recently used.
            }

            offset = (offset + 4) % bytesPerLine; // Next burst address
        }
    }

    public int getHits() { return hits; }

    public int getMisses() { return misses; }

    public float getHitPercentage() {
        return (hits / (float) (hits + misses)) * 100;
    }
}
