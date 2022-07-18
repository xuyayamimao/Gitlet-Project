package gitlet;

import edu.princeton.cs.algs4.BinarySearch;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    public static void setupInit(){
        if (GITLET_DIR.exists()){
            Main.exitWithError("A Gitlet version-control system already exists in the" +
                    " current directory.");
        }
        GITLET_DIR.mkdir();
        File commits = join(GITLET_DIR, "commits");
        commits.mkdir();
        Commit initial = new Commit();
        byte[] commit0 = serialize(initial);
        String uid = sha1(commit0);
        String commmit0filename = uid + ".txt";
        File initialcommit = join(commits, commmit0filename);
        writeObject(initialcommit, initial);
        File HEAD = join(commits, "HEAD.txt");
        File Main = join(commits, "Main.txt");
        writeContents(Main, uid);
        Head head0 = new Head(uid, "Main");
        writeObject(HEAD, head0);
        File blobs = join(commits, "blobs");
        blobs.mkdir();
        File stageforadd = join(GITLET_DIR, "stageforAddition");
        File stagefordel = join(GITLET_DIR, "stageforDeletion");
        stagefordel.mkdir();
        stageforadd.mkdir();
    }

    public static void setupAdd(String[] args) {
        File file = join(CWD, args[1]);
        if (!file.exists()) {
            Main.exitWithError("File does not exist.");
        }
        File stagingArea = join(GITLET_DIR, "stageforAddition");
        File filecopy = join(stagingArea, args[1]);
        Commit newestCommit = getNewestCommit();
        Blob[] bloblist = (Blob[]) newestCommit.getBlobList().toArray();
        int index = indexOf(bloblist, args[1]);
        if (index == -1){
            if (!filecopy.exists()){
                writeContents(filecopy, readContents(file));
            }
            else {
                if (readContents(file).equals(readContents(filecopy))){
                    return;
                }
                else {
                    writeContents(filecopy, readContents(file));
                }
            }
        }
        else {
            Blob recentVersion = bloblist[index];
            if (!filecopy.exists()){
                if (Utils.sha1(readContents(file)).equals(recentVersion.getBlobID())){
                    return;
                }
                else {
                    writeContents(filecopy, readContents(file));
                }
            }
            else {
                if (Utils.sha1(readContents(file)).equals(recentVersion.getBlobID())){
                    filecopy.delete();
                }
                else {
                    if (Utils.sha1(readContents(file)).equals(recentVersion.getBlobID())){
                        return;
                    }
                    else {
                        writeContents(filecopy, readContents(file));
                    }
                }
            }

        }
    }

    /**
     * Returns the index of the specified file in the Blob array.
     *
     * @param  b the array of Blobs, must be sorted in ascending order
     * @param  filename the search filename
     * @return index of key in array {@code a} if present; {@code -1} otherwise
     *
     * Adapt from the indexOf method in class BinarySearch in package edu.princeton.cs.algs4;
     *
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
    public static int indexOf(Blob[] b, String filename){
        if (b == null){
            return -1;
        }
        int lo = 0;
        int hi = b.length - 1;
        while (lo <= hi){
            int mid = lo + (hi - lo) / 2;
            if (filename.compareTo(b[mid].getFilename()) < 0){
                hi = mid - 1;
            }
            else if (filename.compareTo(b[mid].getFilename()) > 0){
                lo = mid + 1;
            }
            else {
                return mid;
            }
        }
        return -1;
    }

    public static Commit getNewestCommit(){
        File head = join(GITLET_DIR, "Commits", "HEAD.txt");
        Head a = readObject(head, Head.class);
        String commitPath = a.getCommitID() + ".txt";
        File newestCommit = join(GITLET_DIR, "commits", commitPath);
        return readObject(newestCommit, Commit.class);
    }

    public static void setupCommit(String[] args) {

    }








    /* TODO: fill in the rest of this class. */
}
