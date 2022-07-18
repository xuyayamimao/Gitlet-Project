package gitlet;

// TODO: any imports you need here

import edu.princeton.cs.algs4.BinarySearch;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date; // TODO: You'll likely use this in this class

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;

    /**      */
    private Date timestamp;

    /**      */
    private String parent;


    private ArrayList<Blob> blobList;

    public Commit(){
        message = "initial commit";
        timestamp = new Date(0);
        parent = null;
        blobList = null;
    }

    public Commit(String message, Date timestamp, String parent, ArrayList<Blob> blobList){
        this.message = message;
        this.timestamp = timestamp;
        this.parent = parent;
        this.blobList = blobList;
    }

    public Date getTimestamp(){
        return timestamp;
    }

    public String getParent(){
        return parent;
    }

    public ArrayList<Blob> getBlobList(){
        return blobList;
    }

    /*
     * public static int indexOf(int[] a, int key) {
     *         int lo = 0;
     *         int hi = a.length - 1;
     *         while (lo <= hi) {
     *             // Key is in a[lo..hi] or not present.
     *             int mid = lo + (hi - lo) / 2;
     *             if      (key < a[mid]) hi = mid - 1;
     *             else if (key > a[mid]) lo = mid + 1;
     *             else return mid;
     *         }
     *         return -1;
     *     }
     */

    public void addBlob(Blob blob){
        int lo = 0;
        int hi = blobList.toArray().length - 1;
        while (lo <= hi) {
            // Key is in a[lo..hi] or not present.
            int mid = lo + (hi - lo) / 2;
            if      (blob < a[mid]) hi = mid - 1;
            else if (key > a[mid]) lo = mid + 1;
            else return mid;
        }
    }



    /* TODO: fill in the rest of this class. */
}
