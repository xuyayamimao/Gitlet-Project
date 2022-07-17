package gitlet;

import java.io.Serializable;

public class Head implements Serializable {
        private String commitID;
        private String branchname;

        public Head(String commitID, String branchname){
            this.commitID = commitID;
            this.branchname = branchname;
        }
        public String getCommitID(){
            return commitID;
        }

        public String getBranch(){
            return branchname;
        }
}
