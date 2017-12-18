public class AddressRecord {
    public final int       address;
    public final boolean[] busEnableSignals;
    public final int       burstCount;
    public final boolean   writeRead;
    public final boolean   dataControl;
    public final boolean   memoryInputOutput;
    public final int       intervalCount;

    public AddressRecord(int address, boolean[] busEnableSignals, int burstCount, boolean writeRead, boolean dataControl, boolean memoryInputOutput, int intervalCount) {
        this.address           = address;
        this.busEnableSignals  = busEnableSignals.clone();
        this.burstCount        = burstCount;
        this.writeRead         = writeRead;
        this.dataControl       = dataControl;
        this.memoryInputOutput = memoryInputOutput;
        this.intervalCount     = intervalCount;
    }
}
