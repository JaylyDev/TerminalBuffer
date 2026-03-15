package org.jayly;
import java.util.ArrayList;

/**
 * Represents a document. This can either be a file on disk or a new unsaved document store in memory.
 */
// TODO: Add subclasses for disk and in-memory documents if needed.
public class Document {
    private ArrayList<LineGrid> lines;

    public Document() {
        this.lines = new ArrayList<LineGrid>();
    }

    /**
     * Get the full content of the document as a single string.
     */
    public String getContent() {
        StringBuilder sb = new StringBuilder();
        for (LineGrid line : lines) {
            sb.append(line.getText()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Get a specific line of the document.
     */
    public LineGrid getLine(int lineNumber) {
        if (lineNumber < 0 || lineNumber >= lines.size()) {
            return null;
        }
        return lines.get(lineNumber);
    }

    public ArrayList<LineGrid> getLines() {
        return lines;
    }

    public ArrayList<LineGrid> getLines(int startLine, int endLine) {
        if (startLine < 0 || endLine >= lines.size() || startLine > endLine) {
            return null;
        }
        return new ArrayList<LineGrid>(lines.subList(startLine, endLine + 1));
    }

    public void setLine(int lineNumber, LineGrid newLine) {
        if (lineNumber < 0 || lineNumber >= lines.size()) {
            return;
        }
        lines.set(lineNumber, newLine);
    }

    public void insertLine(int lineNumber, LineGrid newLine) {
        lines.add(lineNumber, newLine);
    }

    public void deleteLine(int lineNumber) {
        if (lineNumber < 0 || lineNumber >= lines.size()) {
            return;
        }
        lines.remove(lineNumber);
    }
}