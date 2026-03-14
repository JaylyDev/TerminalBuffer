package org.jayly;

/**
 * Specifies how a document should be saved.
 */
public enum DocumentSaveMode {
    /**
     * The document will be saved to disk.
     */
    DISK,
    /**
     * The document will be temporarily saved to memory. The document will persist until terminal buffer shuts down.
     */
    MEMORY
}
