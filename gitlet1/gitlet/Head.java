package gitlet;

import java.io.Serializable;

public class Head implements Serializable {
    private String commitID;
    private String branchname;

    private TrieSet idSet;

    public Head(String commitID, String branchname) {
        this.commitID = commitID;
        this.branchname = branchname;
        idSet = new TrieSet();
        idSet.add(commitID);
    }

    public String getCommitID() {
        return commitID;
    }

    public void setCommitID(String newID) {
        this.commitID = newID;
    }

    public String getBranch() {
        return branchname;
    }

    public TrieSet getIdSet() {
        return idSet;
    }

    public void addCommitID(String newID) {
        idSet.add(newID);
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }


    @Override
    public String toString() {
        return commitID + "\r\n" + branchname;
    }

}
