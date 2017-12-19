public class Directory {
    private int tag;
    private byte[] bytes;

    public Directory(int lineLength) {
        tag = -1;
        bytes = new byte[lineLength];
    }

    /**
     * Update directory to contain data for the passed tag.
     */
    public void update(int tag) {
        this.tag = tag;
        bytes = getData(tag);
    }

    /**
     * Get data from main memory for the passed tag.
     */
    private byte[] getData(int tag) {
        // Not implemented for this project.
        return new byte[bytes.length];
    }

    public byte getByteAt(int index) {
        return bytes[index];
    }

    public int getTag() { return tag; }
}
