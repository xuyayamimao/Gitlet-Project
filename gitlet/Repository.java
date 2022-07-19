package gitlet;

import java.io.File;

import static gitlet.Utils.*;


import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    public static final File STAGEFOR_ADDITION = join(GITLET_DIR, "stageforAddition");
    public static final File STAGEFOR_DELETION = join(GITLET_DIR, "stageforDeletion");
    public static final File COMMITS = join(GITLET_DIR, "commits");
    public static final File BLOBS = join(COMMITS, "blobs");
    public static final File HEAD = join(GITLET_DIR, "Commits", "HEAD.txt");

    public static void setupInit() {
        if (GITLET_DIR.exists()) {
            Main.exitWithError("A Gitlet version-control system already exists in the"
                    + " current directory.");
        }
        GITLET_DIR.mkdir();
        COMMITS.mkdir();
        Commit initial = new Commit();
        byte[] commit0 = serialize(initial);
        String uid = sha1(commit0);
        String commmit0filename = uid + ".txt";
        File initialcommit = join(COMMITS, commmit0filename);
        writeObject(initialcommit, initial);
        File main = join(COMMITS, "Main.txt");
        writeContents(main, uid);
        Head head0 = new Head(uid, "Main");
        writeObject(HEAD, head0);
        BLOBS.mkdir();
        STAGEFOR_DELETION.mkdir();
        STAGEFOR_ADDITION.mkdir();
    }

    public static void setupAdd(String[] args) {
        File file = join(CWD, args[1]);
        if (!file.exists()) {
            Main.exitWithError("File does not exist.");
        }
        File filecopy = join(STAGEFOR_ADDITION, args[1]);
        Commit newestCommit = getNewestCommit();
        Blob[] bloblist = (Blob[]) newestCommit.getBlobList().toArray();
        int index = indexOf(bloblist, args[1]);
        if (index == -1) {
            if (!filecopy.exists()) {
                writeContents(filecopy, readContents(file));
            } else {
                if (readContents(file).equals(readContents(filecopy))) {
                    return;
                } else {
                    writeContents(filecopy, readContents(file));
                }
            }
        } else {
            Blob recentVersion = bloblist[index];
            if (!filecopy.exists()) {
                if (Utils.sha1(readContents(file)).equals(recentVersion.getBlobID())) {
                    return;
                } else {
                    writeContents(filecopy, readContents(file));
                }
            } else {
                if (Utils.sha1(readContents(file)).equals(recentVersion.getBlobID())) {
                    filecopy.delete();
                } else {
                    if (Utils.sha1(readContents(file)).equals(recentVersion.getBlobID())) {
                        return;
                    } else {
                        writeContents(filecopy, readContents(file));
                    }
                }
            }
        }
    }


    public static void setupLog() {
        logHelper(getNewestCommit(), getNewestCommitID());
    }

    private static void logHelper(Commit a, String id) {
        System.out.println("===");
        System.out.println("commit " + id);
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        System.out.println("Date: " + format.format(a.getTimestamp()));
        System.out.println(a.getMessage());
        System.out.println();
        String parent = a.getParent();
        if (parent == null) {
            return;
        }
        File next = join(GITLET_DIR, "commits", parent + ".txt");
        if (!next.exists()) {
            error("parent doesn't exist.");
        }
        logHelper(readObject(next, Commit.class), parent);
    }


    /**
     * Returns the index of the specified file in the Blob array.
     *
     * @param b        the array of Blobs, must be sorted in ascending order
     * @param filename the search filename
     * @return index of key in array {@code a} if present; {@code -1} otherwise
     * <p>
     * Adapt from the indexOf method in class BinarySearch in package edu.princeton.cs.algs4;
     * <p>
     * public static int indexOf(int[] a, int key) {
     * int lo = 0;
     * int hi = a.length - 1;
     * while (lo <= hi) {
     * // Key is in a[lo..hi] or not present.
     * int mid = lo + (hi - lo) / 2;
     * if      (key < a[mid]) hi = mid - 1;
     * else if (key > a[mid]) lo = mid + 1;
     * else return mid;
     * }
     * return -1;
     * }
     */
    public static int indexOf(Blob[] b, String filename) {
        if (b == null) {
            return -1;
        }
        int lo = 0;
        int hi = b.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (filename.compareTo(b[mid].getFilename()) < 0) {
                hi = mid - 1;
            } else if (filename.compareTo(b[mid].getFilename()) > 0) {
                lo = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    public static Commit getNewestCommit() {
        Head a = readObject(HEAD, Head.class);
        String commitPath = a.getCommitID() + ".txt";
        File newestCommit = join(GITLET_DIR, "commits", commitPath);
        return readObject(newestCommit, Commit.class);
    }

    public static String getNewestCommitID() {
        Head a = readObject(HEAD, Head.class);
        return a.getCommitID();
    }


    public void setupCommit(String[] args) {
        File main = join(COMMITS, "Main.txt");
        if (STAGEFOR_ADDITION.list().length == 0) {
            System.out.println("No changes added to the commit.");
        } else if (STAGEFOR_ADDITION.list().length != 0) {
            Commit current = new Commit(args[1], new Date(),
                    getNewestCommitID(), new ArrayList<>());
            List<String> staged = plainFilenamesIn(STAGEFOR_ADDITION);
            for (String a : staged) {
                File b = new File(a);
                Blob c = new Blob(a, sha1(readContents(b)));
                current.addBlob(c);
                File newblob = join(BLOBS, c.getBlobID() + ".txt");
                writeObject(newblob, c);
                restrictedDelete(a);
            }
            writeObject(HEAD, new Head(sha1(serialize(current)), "Main"));
        }
    }








    /* TODO: fill in the rest of this class. */
}
