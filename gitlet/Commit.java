package gitlet;

// TODO: any imports you need here

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


    private BinarySearchTree<Blob> BlobTree;

    public Commit(){
        message = "initial commit";
        timestamp = new Date(0);
        parent = null;
        BlobTree = null;
    }

    public Commit(String message, Date timestamp, String parent, BinarySearchTree<Blob> BlobTree){
        this.message = message;
        this.timestamp = timestamp;
        this.parent = parent;
        this.BlobTree = BlobTree;
    }



    /* TODO: fill in the rest of this class. */
}
