package mg.itu.util;

public class MapFile {
    private String filename;
    private String extension;
    private byte[] bytes;
    private long size;

    public MapFile(String filename, String extension, byte[] bytes) {
        this.filename = filename;
        this.extension = extension;
        this.bytes = bytes;
        this.size = bytes.length;
    }

    public String getFilename() {
        return filename;
    }

    public String getExtension() {
        return extension;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public long getSize() {
        return size;
    }
}