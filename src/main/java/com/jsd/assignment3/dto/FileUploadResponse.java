package com.jsd.assignment3.dto;

import java.time.LocalDateTime;

public class FileUploadResponse {
    private String name;
    private String version;
    private String path;
    private LocalDateTime createDateTime;
    private int numberOfDownload;
    public FileUploadResponse() {


    }

    public FileUploadResponse(String name, String version, String path, LocalDateTime createDateTime, int numberOfDownload) {
        this.name = name;
        this.version = version;
        this.path = path;
        this.createDateTime = createDateTime;
        this.numberOfDownload = numberOfDownload;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public int getNumberOfDownload() {
        return numberOfDownload;
    }

    public void setNumberOfDownload(int numberOfDownload) {
        this.numberOfDownload = numberOfDownload;
    }
}
