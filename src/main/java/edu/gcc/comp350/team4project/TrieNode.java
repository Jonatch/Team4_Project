package edu.gcc.comp350.team4project;

public class TrieNode {
    private char value;
    private TrieNode[] children;
    private boolean endOfWord;

    public TrieNode(char value) {
        this.value = value;
        this.endOfWord = false;
        children = new TrieNode[45]; //size of 45 to support all characters in course names
    }

    public void setEndOfWord(boolean endOfWord) { this.endOfWord = endOfWord; } //if this is the end of a course name, true
    public boolean getEndOfWord() { return endOfWord; }
    public char getValue() { return value; }
    public TrieNode[] getChildren() { return children; }
}
