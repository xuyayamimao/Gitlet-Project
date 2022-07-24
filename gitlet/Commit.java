package gitlet;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Represents a gitlet commit object.
 * Constructs Commit object with message, timestamp, parent commit 1 and 2,
 * and a map of files contained in this commit.
 * Used to construct commit tree.
 *
 * @author Ziya Xu, Jingzhi Zhou.
 */
public class Commit implements Serializable {
    /**
     *
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
     * The time of when the Commit is made.
     */
    private Date timestamp;

    /**
     * The ID of the parent commit.
     */
    private String parent;

    /**
     * The ID of the parent commit.
     */
    private String parent2;

    /**
     * A map of files contained in this commit.
     */
    private HashMap<String, String> fileHashMap;

    public Commit() {
        message = "initial commit";
        timestamp = new Date(0);
        parent = null;
        fileHashMap = new HashMap<>();
    }

    public Commit(String newMessage, Date newTimestamp,
                  String newParent,
                  HashMap<String, String> newFileHashMap) {
        this.message = newMessage;
        this.timestamp = newTimestamp;
        this.parent = newParent;
        this.fileHashMap = newFileHashMap;
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

    public String getParent2() {
        return parent2;
    }

    public void addParent2(String newParent) {
        this.parent2 = newParent;
    }
}
