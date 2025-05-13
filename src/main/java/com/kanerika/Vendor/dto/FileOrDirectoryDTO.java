package com.kanerika.Vendor.dto;

public class FileOrDirectoryDTO {
    private String name;
    private String type; // "file" or "directory"
    private FileStatDTO stat;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FileStatDTO getStat() {
        return stat;
    }

    public void setStat(FileStatDTO stat) {
        this.stat = stat;
    }
}
