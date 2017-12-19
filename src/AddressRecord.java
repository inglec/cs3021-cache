public class AddressRecord {
    public final int       address;
    public final boolean[] busEnableSignals;
    public final int       burstCount;
    public final boolean   writeRead;
    public final boolean   dataControl;
    public final boolean   memoryInputOutput;
    public final int       intervalCount;

    /**
     * Creates a new address record from word 0 and word 1 of a trace file.
     */
    public AddressRecord(int word0, int word1) {
        int mask; // Mask for isolating bits of word0 and word1.

        // Parse word 0.
        mask = Cache.createMask(23);
        address = (word0 & mask) << 2;

        busEnableSignals = new boolean[4];
        busEnableSignals[0] = ((word0 >> 23) & 1) == 1;
        busEnableSignals[1] = ((word0 >> 24) & 1) == 1;
        busEnableSignals[2] = ((word0 >> 25) & 1) == 1;
        busEnableSignals[3] = ((word0 >> 26) & 1) == 1;

        mask = Cache.createMask(2);
        burstCount = ((word0 >> 27) & mask) + 1;

        writeRead         = ((word0 >> 29) & 1) == 1;
        dataControl       = ((word0 >> 30) & 1) == 1;
        memoryInputOutput = ((word0 >> 31) & 1) == 1;

        // Parse word 1.
        mask = Cache.createMask(8);
        intervalCount = word1 & mask;
    }
}
