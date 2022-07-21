package gitlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static gitlet.Utils.*;


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
    public static final File HEAD = join(COMMITS, "HEAD.txt");

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
        /*List<Blob> bloblist = newestCommit.getBlobList();
        int index = indexOf(bloblist, args[1]);*/
        HashMap<String, String> newestHashmap = newestCommit.getFileHashMap();
        if (!newestHashmap.containsKey(args[1])) {
                writeContents(filecopy, readContents(file));
        } else {
            String recentVersion = newestHashmap.get(args[1]);
            if (!filecopy.exists()) {
                if (!Utils.sha1(readContents(file)).equals(recentVersion)) {
                    writeContents(filecopy, readContents(file));
                }
            } else {
                if (Utils.sha1(readContents(file)).equals(recentVersion)) {
                    filecopy.delete();
                }  else {
                    writeContents(filecopy, readContents(file));
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
     * @param blobs    the arrayList of Blobs, must be sorted in ascending order
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
    public static int indexOf(List<Blob> blobs, String filename) {
        if (blobs == null) {
            return -1;
        }
        int lo = 0;
        int hi = blobs.size() - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (filename.compareTo(blobs.get(mid).getFilename()) < 0) {
                hi = mid - 1;
            } else if (filename.compareTo(blobs.get(mid).getFilename()) > 0) {
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

    public static String getBranch() {
        return readObject(HEAD, Head.class).getBranch();
    }


    public static void setupCommit(String[] args) {
        File branch = join(COMMITS, getBranch() + ".txt");
        List<String> staged = plainFilenamesIn(STAGEFOR_ADDITION);
        if (staged.size() == 0) {
            Main.exitWithError("No changes added to the commit.");
        } else {
            HashMap<String, String> defaultFilemap = getNewestCommit().getFileHashMap();
            Commit current = new Commit(args[1], new Date(),
                    getNewestCommitID(), defaultFilemap);
            for (String a : staged) {
                File b = join(STAGEFOR_ADDITION, a);
                byte[] content = readContents(b);
                String id = sha1(content);
                File version = join(BLOBS, id + ".txt");
                writeContents(version, content);
                if (defaultFilemap.containsKey(a)){
                    defaultFilemap.replace(a, id);
                } else {
                    defaultFilemap.put(a, id);
                }
                b.delete();
            }
            String uid = sha1(serialize(current));
            String commmitFilename = uid + ".txt";
            File commit = join(COMMITS, commmitFilename);
            writeObject(commit, current);
            writeContents(branch, uid);
            writeObject(HEAD, new Head(uid, getBranch()));
        }

    }

    public static void setupCheckout1(String[] args) {
        String filename = args[2];
        HashMap<String, String> newestFilemap = getNewestCommit().getFileHashMap();
        if (!newestFilemap.containsKey(filename)) {
            Main.exitWithError("File does not exist in that commit.");
        }
        File tothisVersion = join(BLOBS, newestFilemap.get(filename) + ".txt");
        File toBeChanged = join(CWD, filename);
        writeContents(toBeChanged, readContents(tothisVersion));
    }

    public static void setupCheckout2(String[] args) {
        File commitfile = join(COMMITS, args[1] + ".txt");
        if (!commitfile.exists()) {
            Main.exitWithError("No commit with that id exists.");
        }
        Commit a = readObject(commitfile, Commit.class);
        HashMap<String, String> tothisFilemap = getNewestCommit().getFileHashMap();
        if (!tothisFilemap.containsKey(args[3])) {
            Main.exitWithError("File does not exist in that commit.");
        }
        File tothisVersion = join(BLOBS, tothisFilemap.get(args[3]) + ".txt");
        File toBeChanged = join(CWD, args[3]);
        writeContents(toBeChanged, readContents(tothisVersion));
    }

    /* TODO: fill in the rest of this class. */
}
