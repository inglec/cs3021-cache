import java.util.LinkedList;

public class Set {
    private Directory[] directories;
    private LinkedList<Integer> leastRecentlyUsed; // Stores the index of the least recently used directory.

    /**
     * Creates a set containing an array of directories.
     */
    public Set(int directories, int lineLength) {
        this.directories = new Directory[directories];
        for (int i = 0; i < directories; i++) {
            this.directories[i] = new Directory(lineLength);
        }

        // Initialise queue of least recently used directories.
        leastRecentlyUsed = new LinkedList<Integer>();
        for (int i = 0; i < directories; i++) {
            leastRecentlyUsed.add(i);
        }
    }

    /**
     * Update the least recently used directory to contain the passed tag.
     */
    public void update(int tag) {
        int victim = getLeastRecentlyUsed();
        directories[victim].update(tag);
    }

    /**
     * Gets least recently used directory index and adds it to end of queue.
     */
    public int getLeastRecentlyUsed() {
        int lru = leastRecentlyUsed.remove();
        leastRecentlyUsed.add(lru);

        return lru;
    }

    /**
     * Remove directory index and add to end of queue.
     */
    public void updateLeastRecentlyUsed(int directoryIndex) {
        for (int i = 0; i < leastRecentlyUsed.size(); i++) {
            if (leastRecentlyUsed.get(i) == directoryIndex) {
                leastRecentlyUsed.remove(i);
                leastRecentlyUsed.add(directoryIndex);
                return;
            }
        }
    }

    public boolean containsTag(int tag) {
        for (int i = 0; i < directories.length; i++) {
            if (directories[i].getTag() == tag)
                return true;
        }

        return false;
    }

    public byte getData(int tag, int offset) {
        for (int i = 0; i < directories.length; i++) {
            if (directories[i].getTag() == tag) {
                updateLeastRecentlyUsed(i); // Move directory to end of queue.
                return directories[i].getByteAt(offset);
            }
        }

        System.out.println("Tag not found within set. Updating.");
        update(tag);
        return -1;
    }
}
