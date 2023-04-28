package edu.gcc.comp350.team4project;

public class WordTree {
    private TrieNode root;
    public WordTree() { root = new TrieNode(' '); }

    public void addCourse(ScheduleElement c) {
        String courseName = c.getName().toUpperCase();
        TrieNode ptr = root;
        for (int i = 0; i < courseName.length(); i++) {
            char val = courseName.charAt(i);
            int index = getIndex(val);
            if (ptr.getChildren()[index] == null) {
                TrieNode s = new TrieNode(val);
                ptr.getChildren()[index] = s;
                ptr = s;
            }
            else ptr = ptr.getChildren()[index];
        }
        ptr.setEndOfWord(true);
    }

    public boolean containsCourse(ScheduleElement c) { //for testing
        String elementName = c.getName().toUpperCase();
        TrieNode ptr = root;
        for (int i = 0; i < elementName.length(); i++) {
            char val = elementName.charAt(i);
            int index = getIndex(val);
            if (ptr.getChildren()[index] != null)
                ptr = ptr.getChildren()[index];
            else return false;
        }
        return true;
    }

    public void printContents(String subWord, TrieNode c) { //for testing
        if (c.getEndOfWord()) System.out.println(subWord);

        for (int i = 0; i < c.getChildren().length; i++) {
            TrieNode node = c.getChildren()[i];
            if (node != null) printContents(subWord + node.getValue(), node);
        }
    }

    private int getIndex(char val) {
        return switch (val) {
            case ' ' -> 26;
            case '/' -> 27;
            case '&' -> 28;
            case ':' -> 29;
            case '-' -> 30;
            case '\'' -> 31;
            case '.' -> 32;
            case '1' -> 33;
            case '2' -> 34;
            case '3' -> 35;
            case '4' -> 36;
            case '5' -> 37;
            case '6' -> 38;
            case '7' -> 39;
            case '8' -> 40;
            case '9' -> 41;
            case '0' -> 42;
            default -> val - 'A';
        };
    }

    public TrieNode getRoot() { return root; }
}
