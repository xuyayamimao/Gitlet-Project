package gitlet;

import edu.princeton.cs.algs4.BinarySearch;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;

import java.util.Date;

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

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    public static void setupInit() {
        if (GITLET_DIR.exists()) {
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
        File stageforadd = join(GITLET_DIR, "stageforAddition");
        File filecopy = join(stageforadd, args[1]);
        if (!filecopy.exists()) {
            Commit b = getNewestCommit();
            Blob[] bloblist = (Blob[]) b.getBlobList().toArray();
            BinarySearch.indexOf();
            writeContents(filecopy, readContents(file));
        }
    }

    public static Commit getNewestCommit() {
        File head = join(GITLET_DIR, "Commits", "HEAD.txt");
        Head a = readObject(head, Head.class);
        String commitPath = a.getCommitID() + ".txt";
        File newestCommit = join(GITLET_DIR, "commits", commitPath);
        Commit b = readObject(newestCommit, Commit.class);
        return b;
    }


    public void setupCommit(String[] args) {
        File stageforadd = join(GITLET_DIR, "stageforAddition");
        File stagefordel = join(GITLET_DIR, "stageforDeletion");
        if (stageforadd.list().length == 0) {
            Commit a = new Commit(args[1], new Date(),
                    getNewestCommit().,
                    getNewestCommit().getBlobList());
    }
}








    /* TODO: fill in the rest of this class. */
}
