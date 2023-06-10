package gitlet;

import java.io.Serializable;

public class Blob implements Serializable, Comparable<Blob> {
    private String filename;
    private String blobID;

    public Blob(String filename, String blobID) {
        this.filename = filename;
        this.blobID = blobID;
    }

    public String getFilename() {
        return filename;
    }

    public String getBlobID() {
        return blobID;
    }

    public void setBlobID(String a) {
        this.blobID = a;
    }

    @Override
    public int compareTo(Blob o) {
        return filename.compareTo(o.filename);
    }
}
