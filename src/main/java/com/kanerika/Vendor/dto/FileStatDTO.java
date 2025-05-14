package com.kanerika.Vendor.dto;

public class FileStatDTO {
    private long size;
    private long directoryPath;
    private String permissions;
    private int ownerId;
    private int groupId;
    private int lastModifiedTime;

    public long getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(long directoryPath) {
        this.directoryPath = directoryPath;
    }

    // Getters and Setters
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(int lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
}
