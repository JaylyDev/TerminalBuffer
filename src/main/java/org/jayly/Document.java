import java.util.ArrayList;

/**
 * Represents a document. This can either be a file on disk or a new unsaved document store in memory.
 */
// TODO: Add subclasses for disk and in-memory documents if needed.
public class Document {
    private String content;
    private ArrayList<String> lines;
    private DocumentSaveMode saveMode;
    private String filePath;
    private String fileName;

    public Document(String content, DocumentSaveMode saveMode) {
        this.content = content;
        this.saveMode = saveMode;
        this.lines = new ArrayList<>();
        for (String line : content.split("\n")) {
            this.lines.add(line);
        }
    }

    public String getContent() {
        return content;
    }

    public String getLine(Integer lineNumber) {
        if (lineNumber < 0 || lineNumber >= lines.size()) {
            return null;
        }
        return lines.get(lineNumber);
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    public ArrayList<String> getLines(Integer startLine, Integer endLine) {
        if (startLine < 0 || endLine >= lines.size() || startLine > endLine) {
            return null;
        }
        return new ArrayList<String>(lines.subList(startLine, endLine + 1));
    }

    public void setLine(Integer lineNumber, String newLine) {
        if (lineNumber < 0 || lineNumber >= lines.size()) {
            return;
        }
        lines.set(lineNumber, newLine);
        this.content = String.join("\n", lines);
    }

    public void insertLine(Integer lineNumber, String newLine) {
        lines.add(lineNumber, newLine);
        this.content = String.join("\n", lines);
    }

    public void deleteLine(Integer lineNumber) {
        if (lineNumber < 0 || lineNumber >= lines.size()) {
            return;
        }
        lines.remove(lineNumber);
        this.content = String.join("\n", lines);
    }

    public DocumentSaveMode getSaveMode() {
        return saveMode;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }
}