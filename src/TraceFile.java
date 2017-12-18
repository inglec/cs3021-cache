import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public class TraceFile {
    public static final int BUFFER_SIZE = Integer.BYTES; // Load 4 bytes into buffer each time.

    private ArrayList<AddressRecord> addressRecords = new ArrayList<AddressRecord>();

    public TraceFile(String filename) {
        File file = new File(filename);

        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[BUFFER_SIZE];

            // Read in two 32-bit integers at a time from file.
            int returnCode = inputStream.read(buffer); // Fill buffer with word0.
            while (returnCode != -1) {
                int word0 = ByteBuffer.wrap(buffer).getInt();

                int mask = Cache.createMask(23);
                int address = (word0 & mask) << 2;

                boolean[] busEnableSignals = new boolean[4];
                busEnableSignals[0] = ((word0 >> 23) & 1) == 1;
                busEnableSignals[1] = ((word0 >> 24) & 1) == 1;
                busEnableSignals[2] = ((word0 >> 25) & 1) == 1;
                busEnableSignals[3] = ((word0 >> 26) & 1) == 1;

                mask = Cache.createMask(2);
                int burstCount = ((word0 >> 27) & mask) + 1;

                boolean writeRead         = ((word0 >> 29) & 1) == 1;
                boolean dataControl       = ((word0 >> 30) & 1) == 1;
                boolean memoryInputOutput = ((word0 >> 31) & 1) == 1;

                // Fill buffer and get word 1.
                returnCode = inputStream.read(buffer);
                if (returnCode != -1) {
                    int word1 = ByteBuffer.wrap(buffer).getInt();

                    mask = Cache.createMask(8);
                    int intervalCount = word1 & mask;

                    addressRecords.add(new AddressRecord(address, busEnableSignals, burstCount, writeRead, dataControl, memoryInputOutput, intervalCount));

                    returnCode = inputStream.read(buffer); // Fill buffer with next word0.
                }
            }

            System.out.println("Successfully loaded " + addressRecords.size() + " records from " + filename);

            inputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Iterator<AddressRecord> getAddressRecords() {
        return addressRecords.iterator();
    }
}
