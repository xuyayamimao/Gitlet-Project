package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /**
     * The message of this Commit.
     */
    private String message;

    /**
     *
     */
    private Date timestamp;

    /**
     *
     */
    private String parent;

    /**
     *
     */
    private HashMap<String, String> fileHashMap;

    public Commit() {
        message = "initial commit";
        timestamp = new Date(0);
        parent = null;
        fileHashMap = new HashMap<>();
    }

    public Commit(String message, Date timestamp, String parent, HashMap<String, String> fileHashMap) {
        this.message = message;
        this.timestamp = timestamp;
        this.parent = parent;
        this.fileHashMap = fileHashMap;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getParent() {
        return parent;
    }

    public HashMap<String, String> getFileHashMap() {
        return fileHashMap;
    }

/*
    public void addBlob(Blob blob) {
        if (blobList == null) {
            blobList = new ArrayList<Blob>();
        }
        int lo = 0;
        int hi = blobList.size() - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (blob.compareTo(blobList.get(mid)) < 0) {
                hi = mid - 1;
            } else if (blob.compareTo(blobList.get(mid)) > 0) {
                lo = mid + 1;
            } else {
                blobList.set(mid, blob);
                return;
            }
        }
        blobList.add(lo, blob);
    }*/

    /* TODO: fill in the rest of this class. */
}
