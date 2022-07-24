package gitlet;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author Ziya Xu, Jingzhi Zhou
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {

        if (args.length == 0) {
            exitWithError("Please enter a command.");
        }


        String firstArg = args[0];
        switch (firstArg) {
            case "init" -> {
                validateNumArgs("init", args, 1);
                Repository.setupInit();
            }
            case "add" -> {
                gitletExist();
                validateNumArgs("add", args, 2);
                Repository.setupAdd(args[1]);
            }
            case "commit" -> {
                gitletExist();
                validateNumArgs("commit", args, 2);
                if (args[1].equals("")) {
                    exitWithError("Please enter a commit message.");
                }
                Repository.setupCommit(args[1], "");
            }
            case "log" -> {
                gitletExist();
                validateNumArgs("log", args, 1);
                Repository.setupLog();
            }
            case "global-log" -> {
                gitletExist();
                validateNumArgs("log", args, 1);
                Repository.setupGlobalLog();
            }
            case "checkout" -> {
                gitletExist();
                if (args.length == 3) {
                    if (!args[1].equals("--")) {
                        exitWithError("Incorrect operands.");
                    }
                    Repository.setupCheckout1(args[2]);
                } else if (args.length == 4) {
                    if (!args[2].equals("--")) {
                        exitWithError("Incorrect operands.");
                    }
                    Repository.setupCheckout2(args[1], args[3]);
                } else if (args.length == 2) {
                    Repository.setupCheckout3(args[1]);
                } else {
                    exitWithError("Incorrect operands.");
                }
            }
            case "rm" -> {
                gitletExist();
                validateNumArgs("rm", args, 2);
                Repository.setupRemove(args[1]);
            }
            case "find" -> {
                gitletExist();
                validateNumArgs("find", args, 2);
                Repository.setupFind(args[1]);
            }
            case "status" -> {
                gitletExist();
                validateNumArgs("status", args, 1);
                Repository.setupStatus();
            }
            case "branch" -> {
                gitletExist();
                validateNumArgs("branch", args, 2);
                Repository.setupBranch(args[1]);
            }
            case "rm-branch" -> {
                gitletExist();
                validateNumArgs("rm-branch", args, 2);
                Repository.setupRmBranch(args[1]);
            }
            case "reset" -> {
                gitletExist();
                validateNumArgs("reset", args, 2);
                Repository.setupReset(args[1]);
            }
            case "merge" -> {
                gitletExist();
                validateNumArgs("merge", args, 2);
                Repository.setupMerge(args[1]);
            }
            default -> exitWithError("No command with that name exists.");
        }
    }

    public static void exitWithError(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
        System.exit(0);
    }

    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     *
     * @param cmd  Name of command you are validating
     * @param args Argument array from command line
     * @param n    Number of expected arguments
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            exitWithError("Incorrect operands.");
        }
    }

    /**
     * Checks if there exists an initialized Gitlet working
     * directory, throws a RuntimeException if there isn't
     * Gitlet working directory.
     */
    public static void gitletExist() {
        if (!Repository.GITLET_DIR.exists()) {
            exitWithError("Not in an initialized Gitlet directory.");
        }
    }
}
