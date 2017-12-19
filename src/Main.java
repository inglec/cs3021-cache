import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        Cache instructionCache = new Cache(16, 1, 1024);
        Cache dataCache        = new Cache(16, 8, 256);

        TraceFile traceFile = new TraceFile("input/gcc1.trace");
        Iterator<AddressRecord> addressRecords = traceFile.getAddressRecords();

        // Hit caches with each address record in the trace file.
        while (addressRecords.hasNext()) {
            AddressRecord record = addressRecords.next();

            boolean instructionRead = record.memoryInputOutput && !record.dataControl && !record.writeRead;
            boolean dataReadOrWrite = record.memoryInputOutput &&  record.dataControl;

            if (instructionRead) {
                instructionCache.hit(record.address, record.burstCount);
            }
            else if (dataReadOrWrite) {
                dataCache.hit(record.address, record.burstCount);
            }
        }

        // Output hit rate of each cache to console.
        System.out.println("Instruction Cache: "
                + String.format("%,d", instructionCache.getHits()) + " hits, "
                + String.format("%,d", instructionCache.getMisses()) + " misses "
                + "(" + instructionCache.getHitPercentage() + "%).");

        System.out.println("Data Cache: "
                + String.format("%,d", dataCache.getHits()) + " hits, "
                + String.format("%,d", dataCache.getMisses()) + " misses "
                + "(" + dataCache.getHitPercentage() + "%).");
    }
}
