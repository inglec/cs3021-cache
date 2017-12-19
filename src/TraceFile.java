import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class TraceFile {
    public static final int BUFFER_SIZE = Integer.BYTES; // Load 4 bytes into buffer each time.

    private Queue<AddressRecord> addressRecords = new LinkedList<AddressRecord>();

    public TraceFile(String filename) {
        File file = new File(filename);

        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[BUFFER_SIZE];

            // Read in two 32-bit integers at a time from file.
            int returnCode = inputStream.read(buffer); // Fill buffer with word0.
            while (returnCode != -1) {
                int word0 = ByteBuffer.wrap(buffer).getInt();

                inputStream.read(buffer); // Fill buffer and get word 1.
                int word1 = ByteBuffer.wrap(buffer).getInt();

                addressRecords.add(new AddressRecord(word0, word1));

                returnCode = inputStream.read(buffer); // Fill buffer with next word0.
            }
            inputStream.close();

            System.out.println("Successfully loaded " + String.format("%,d", addressRecords.size())
                    + " records from " + filename);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Iterator<AddressRecord> getAddressRecords() {
        return addressRecords.iterator();
    }
}
