package gitlet;

import edu.princeton.cs.algs4.BinarySearch;

import java.util.ArrayList;

public class BinarySearchTree<T extends Comparable<T>> {

    protected TreeNode root;

    public BinarySearchTree() {
        root = null;
    }

    public BinarySearchTree(TreeNode t) {
        root = t;
    }

    /* Returns true if the BST contains the given KEY. */
    public boolean contains(T key) {
        return containsHelper(root, key);
    }

    private boolean containsHelper(TreeNode t, T key){
        if (t == null){
            return false;
        }
        if (t.item == key){
            return true;
        }
        if (key.compareTo(t.item) < 0){
            return containsHelper(t.left, key);
        }
        else {
            return containsHelper(t.right, key);
        }
    }

    /* Adds a node for KEY iff KEY isn't in the BST already. */
    public void add(T key) {
        if (root == null){
            root = new TreeNode(key);
            return;
        }
        addsHelper(root, key);
    }

    private void addsHelper(TreeNode t, T key){
        if (t.item == key){
            return;
        }
        if (key.compareTo(t.item) < 0){
            if (t.left == null){
                t.left = new TreeNode(key);
                return;
            }
            addsHelper(t.left, key);
        }
        else {
            if (t.right == null){
                t.right = new TreeNode(key);
                return;
            }
            addsHelper(t.right, key);
        }
    }

    /* Deletes a node from the BST.
     * Even though you do not have to implement delete, you
     * should read through and understand the basic steps.
     */
    public T delete(T key) {
        TreeNode parent = null;
        TreeNode curr = root;
        TreeNode delNode = null;
        TreeNode replacement = null;
        boolean rightSide = false;

        while (curr != null && !curr.item.equals(key)) {
            if (curr.item.compareTo(key) > 0) {
                parent = curr;
                curr = curr.left;
                rightSide = false;
            } else {
                parent = curr;
                curr = curr.right;
                rightSide = true;
            }
        }
        delNode = curr;
        if (curr == null) {
            return null;
        }

        if (delNode.right == null) {
            if (root == delNode) {
                root = root.left;
            } else {
                if (rightSide) {
                    parent.right = delNode.left;
                } else {
                    parent.left = delNode.left;
                }
            }
        } else {
            curr = delNode.right;
            replacement = curr.left;
            if (replacement == null) {
                replacement = curr;
            } else {
                while (replacement.left != null) {
                    curr = replacement;
                    replacement = replacement.left;
                }
                curr.left = replacement.right;
                replacement.right = delNode.right;
            }
            replacement.left = delNode.left;
            if (root == delNode) {
                root = replacement;
            } else {
                if (rightSide) {
                    parent.right = replacement;
                } else {
                    parent.left = replacement;
                }
            }
        }
        return delNode.item;
    }

    protected class TreeNode {

        T item;
        TreeNode left;
        TreeNode right;
        int size = 0;

        public TreeNode(T item) {
            this.item = item; left = right = null;
        }

        public TreeNode(T item, TreeNode left, TreeNode right) {
            this.item = item;
            this.left = left;
            this.right = right;
        }


    }
}
