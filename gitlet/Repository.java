package gitlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public static final File HEAD = join(GITLET_DIR, "HEAD.txt");
    public static final File BRANCHES = join(GITLET_DIR, "branches");
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
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
        BRANCHES.mkdir();
        File main = join(BRANCHES, "main.txt");
        writeContents(main, uid);
        Head head0 = new Head(uid, "main");
        writeObject(HEAD, head0);
        BLOBS.mkdir();
        STAGEFOR_DELETION.mkdir();
        STAGEFOR_ADDITION.mkdir();
    }

    //need to consider stage for del
    public static void setupAdd(String[] args) {
        File file = join(CWD, args[1]);
        if (!file.exists()) {
            Main.exitWithError("File does not exist.");
        }
        File inStagefordel = join(STAGEFOR_DELETION, args[1]);
        if (inStagefordel.exists()) {
            inStagefordel.delete();
        }
        File filecopy = join(STAGEFOR_ADDITION, args[1]);
        Commit newestCommit = getNewestCommit();
        /*List<Blob> bloblist = newestCommit.getBlobList();
        int index = indexOf(bloblist, args[1]);*/
        HashMap<String, String> newestHashmap = newestCommit.getFileHashMap();

        // don't have this file in current commit
        if (!newestHashmap.containsKey(args[1])) {
                writeContents(filecopy, readContents(file));
        }
        // have this file in current commit
        else {
            String recentVersion = newestHashmap.get(args[1]);

            // don't have this file in stage
            if (!filecopy.exists()) {
                if (!Utils.sha1(readContents(file)).equals(recentVersion)) {
                    writeContents(filecopy, readContents(file));
                }
            }
            // have this file in stage
            else {
                if (Utils.sha1(readContents(file)).equals(recentVersion)) {
                    filecopy.delete();
                }  else {
                    writeContents(filecopy, readContents(file));
                }
            }
        }
    }

    public static void setupRemove(String filename) {
        File inStageforAdd = join(STAGEFOR_ADDITION, filename);
        int count = 0;
        if (inStageforAdd.exists()) {
            inStageforAdd.delete();
            count++;
        }
        if (getNewestCommit().getFileHashMap().containsKey(filename)) {
            File inStageforDel = join(STAGEFOR_DELETION, filename);
            writeContents(inStageforDel, "");
            File inCWD = join(CWD, filename);
            if (inCWD.exists()) {
                restrictedDelete(inCWD);
            }
            count++;
        }
        if (count == 0) {
            Main.exitWithError("No reason to remove the file.");
        }
    }

    //need to adjust with branch function
    public static void setupLog() {
        logHelper(getNewestCommit(), getNewestCommitID());
    }

    //need to adjust with branch function
    public static void setupGlobalLog() {
        for (String a : plainFilenamesIn(COMMITS)){
            if (a.length() > 40) {
                System.out.println("===");
                System.out.println("commit " + a.substring(0, 40));
                Commit current = readObject(join(COMMITS, a), Commit.class);
                System.out.println("Date: " + FORMAT.format(current.getTimestamp()));
                System.out.println(current.getMessage());
                System.out.println();
            }
        }
    }

    public static void setupFind(String message){
        int count = 0;
        for (String filename : plainFilenamesIn(COMMITS)){
            if (filename.length() > 40){
                Commit current = readObject(join(COMMITS, filename), Commit.class);
                if (current.getMessage().equals(message)){
                    System.out.println(filename.substring(0, 40));
                    count++;
                }
            }
        }
        if (count == 0){
            Main.exitWithError("Found no commit with that message.");
        }
    }

    //need to adjust with branch function
    public static void setupCommit(String[] args) {
        File branch = join(BRANCHES, getBranch() + ".txt");
        List<String> stagedforadd = plainFilenamesIn(STAGEFOR_ADDITION);
        List<String> stagedfordel = plainFilenamesIn(STAGEFOR_DELETION);
        if (stagedforadd.size() == 0  && stagedfordel.size() == 0) {
            Main.exitWithError("No changes added to the commit.");
        }
        else {
            HashMap<String, String> defaultFilemap = getNewestCommit().getFileHashMap();
            Commit current = new Commit(args[1], new Date(), getNewestCommitID(), defaultFilemap);
            if (stagedforadd.size() != 0) {
                for (String a : stagedforadd) {
                    File b = join(STAGEFOR_ADDITION, a);
                    byte[] content = readContents(b);
                    String id = sha1(content);
                    File version = join(BLOBS, id + ".txt");
                    writeContents(version, content);
                    if (defaultFilemap.containsKey(a)) {
                        defaultFilemap.replace(a, id);
                    } else {
                        defaultFilemap.put(a, id);
                    }
                    b.delete();
                }
            }
            if (stagedfordel.size() != 0){
                for (String a : stagedfordel) {
                    File b = join(STAGEFOR_DELETION, a);
                    defaultFilemap.remove(a);
                    b.delete();
                }
            }
            String uid = sha1(serialize(current));
            String commmitFilename = uid + ".txt";
            File commit = join(COMMITS, commmitFilename);
            writeObject(commit, current);
            writeContents(branch, uid);
            writeObject(HEAD, new Head(uid, getBranch()));
        }

    }

    //need to adjust with branch function
    //need to
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
        HashMap<String, String> tothisFilemap = a.getFileHashMap();
        if (!tothisFilemap.containsKey(args[3])) {            Main.exitWithError("File does not exist in that commit.");
        }
        File tothisVersion = join(BLOBS, tothisFilemap.get(args[3]) + ".txt");
        File toBeChanged = join(CWD, args[3]);
        writeContents(toBeChanged, readContents(tothisVersion));
    }



    public static void setupStatus(){
        System.out.println("=== Branches ===");
        String currentBranch = getBranch();
        for (String a  : plainFilenamesIn(BRANCHES)){
            String toPrint = a.substring(0, a.length() - 4);
            if (toPrint.equals(currentBranch)){
                System.out.println("*" + toPrint);
            }
            else {
                System.out.println(toPrint);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (String a : plainFilenamesIn(STAGEFOR_ADDITION)){
            System.out.println(a);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String a : plainFilenamesIn(STAGEFOR_DELETION)){
            System.out.println(a);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");

        HashMap<String, String> newestHashmap = getNewestCommit().getFileHashMap();
        LinkedList<String> listOfMod = new LinkedList<>();
        LinkedList<String> listOfUntracked = new LinkedList<>();

        // exist in CWD
        for (String a : plainFilenamesIn(CWD)){
            File inCwd = join(CWD, a);
            File inStage = join(STAGEFOR_ADDITION, a);
            // exist in stage
            if (inStage.exists()){
                if (!sha1(readContents(inCwd)).equals(sha1(readContents(inStage)))){
                    listOfMod.add(a + " (modified)");
                }
            }
            // don't exist in stage
            else {

                // have this file in current commit
                if (newestHashmap.containsKey(a)) {
                    String recentVersion = newestHashmap.get(a);
                    // have different contents
                    if (!Utils.sha1(readContents(inCwd)).equals(recentVersion)) {
                        listOfMod.add(a + " (modified)");
                    }
                }
                // don't have this file in current commit
                else {
                    listOfUntracked.add(a);
                }
            }
        }

        //exist in stage
        for (String a : plainFilenamesIn(STAGEFOR_ADDITION)) {
            File inCwd = join(CWD, a);
            File inStage = join(STAGEFOR_ADDITION, a);

            // Doesn't exist in CWD
            if (!inCwd.exists()){
                listOfMod.add(a + " (deleted)");
            }
        }

        //exist in commit
        for (String a : newestHashmap.keySet()){
            File inCwd = join(CWD, a);
            File inRmStage = join(STAGEFOR_DELETION, a);
            if (!inCwd.exists() && !inRmStage.exists()){
                listOfMod.add(a + " (deleted)");
            }
        }

        String[] mod = listOfMod.toArray(new String[listOfMod.size()]);
        Arrays.sort(mod);
        for (String a : mod){
            System.out.println(a);
        }
        System.out.println();
        System.out.println("=== Untracked Files ===");
        for (String a : listOfUntracked){
            System.out.println(a);
        }
    }

    //need to adjust with branch function
    private static void logHelper(Commit a, String id) {
        System.out.println("===");
        System.out.println("commit " + id);
        System.out.println("Date: " + FORMAT.format(a.getTimestamp()));
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
    /*public static int indexOf(List<Blob> blobs, String filename) {
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
    }*/

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

    /* TODO: fill in the rest of this class. */
}
