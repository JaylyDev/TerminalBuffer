package org.jayly;
import java.util.ArrayList;

/**
 * Represents a document. This can either be a file on disk or a new unsaved document store in memory.
 */
// TODO: Add subclasses for disk and in-memory documents if needed.
public class Document {
    private String content;
    private ArrayList<String> lines;

    public Document(String content) {
        this.content = content;
        this.lines = new ArrayList<>();
        for (String line : content.split("\n")) {
            this.lines.add(line);
        }
    }

    public String getContent() {
        return content;
    }

    public String getLine(int lineNumber) {
        if (lineNumber < 0 || lineNumber >= lines.size()) {
            return null;
        }
        return lines.get(lineNumber);
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    public ArrayList<String> getLines(int startLine, int endLine) {
        if (startLine < 0 || endLine >= lines.size() || startLine > endLine) {
            return null;
        }
        return new ArrayList<String>(lines.subList(startLine, endLine + 1));
    }

    public void setLine(int lineNumber, String newLine) {
        if (lineNumber < 0 || lineNumber >= lines.size()) {
            return;
        }
        lines.set(lineNumber, newLine);
        this.content = String.join("\n", lines);
    }

    public void insertLine(int lineNumber, String newLine) {
        lines.add(lineNumber, newLine);
        this.content = String.join("\n", lines);
    }

    public void deleteLine(int lineNumber) {
        if (lineNumber < 0 || lineNumber >= lines.size()) {
            return;
        }
        lines.remove(lineNumber);
        this.content = String.join("\n", lines);
    }
}