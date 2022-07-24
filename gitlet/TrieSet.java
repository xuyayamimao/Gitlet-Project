package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class TrieSet implements Serializable {

    private Trienode sentinel;


    public TrieSet() {
        sentinel = new Trienode();
    }

    public void clear() {
        sentinel = new Trienode();
    }

    public boolean contains(String key) {
        return sentinel.containString(key);
    }

    public boolean containsPrefix(String prefix) {
        return sentinel.containPrefix(prefix);
    }

    public void add(String key) {
        sentinel.addString(key);
    }

    public List<String> keysWithPrefix(String prefix) {
        return sentinel.prefixKeys(prefix);
    }

    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException("Unsupported");
    }

    private class Trienode implements Serializable {
        char item;
        boolean endOfString;
        HashMap<Character, Trienode> children = new HashMap<>();

        Trienode() {
            item = '\u0000';
            endOfString = false;
        }

        Trienode(char item, boolean endOfString) {
            this.item = item;
            this.endOfString = endOfString;
        }

        public void setEndOfString() {
            endOfString = true;
        }

        public void addString(String a) {
            if (a == null || a.equals("")) {
                throw new IllegalArgumentException("input string is null or empty");
            }
            char first = a.charAt(0);
            if (a.length() == 1) {
                if (children.containsKey(first)) {
                    children.get(first).setEndOfString();
                } else {
                    children.put(first, new Trienode(first, true));
                }
            } else {
                String rest = a.substring(1);
                if (children.containsKey(first)) {
                    children.get(first).addString(rest);
                } else {
                    Trienode newNode = new Trienode(first, false);
                    newNode.addString(rest);
                    children.put(first, newNode);
                }
            }
        }

        public boolean containString(String key) {
            if (key == null) {
                throw new IllegalArgumentException("input string is null");
            } else if (key.equals("")) {
                return endOfString;
            } else {
                char first = key.charAt(0);
                if (children.containsKey(first)) {
                    return children.get(first).containString(key.substring(1));
                } else {
                    return false;
                }
            }
        }

        public boolean containPrefix(String prefix) {
            if (prefix == null) {
                throw new IllegalArgumentException("input string is null");
            } else if (prefix.equals("")) {
                return true;
            } else {
                char first = prefix.charAt(0);
                if (children.containsKey(first)) {
                    return children.get(first).containPrefix(prefix.substring(1));
                } else {
                    return false;
                }
            }
        }

        public List<String> prefixKeys(String prefix) {
            if (containPrefix(prefix)) {
                Trienode current = this.toTheEnd(prefix);
                return current.helperPrefix(prefix);
            } else {
                throw new IllegalArgumentException("no keys with this prefix");
            }
        }

        public List<String> helperPrefix(String prefix) {
            List<String> sofar = new ArrayList<>();
            if (endOfString) {
                sofar.add(prefix);
            }
            Set<Character> rest = children.keySet();
            if (rest.isEmpty()) {
                return sofar;
            }
            for (Character i : rest) {
                String newPrefix = prefix + i;
                sofar.addAll(children.get(i).helperPrefix(newPrefix));
            }
            return sofar;
        }

        private Trienode toTheEnd(String prefix) {
            if (prefix.equals("")) {
                return this;
            }
            Trienode next = children.get(prefix.charAt(0));
            return next.toTheEnd(prefix.substring(1));
        }
    }
}
