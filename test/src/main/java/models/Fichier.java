package models;

import jakarta.persistence.*;

@Entity
@Table(name = "fichier")
public class Fichier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filename;
    private String extension;
    private long size;

    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] content;

    // Getters
    public Long getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getExtension() {
        return extension;
    }

    public long getSize() {
        return size;
    }

    public byte[] getContent() {
        return content;
    }

    // Setters
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}