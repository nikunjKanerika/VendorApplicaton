package com.kanerika.Vendor.dto;

import java.util.List;

public class ParentDirectoryStatsResponseDTO {
    private String directoryPath;
    private List<FileStatDTO> parentDirectoriesStats;

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public List<FileStatDTO> getParentDirectoriesStats() {
        return parentDirectoriesStats;
    }

    public void setParentDirectoriesStats(List<FileStatDTO> parentDirectoriesStats) {
        this.parentDirectoriesStats = parentDirectoriesStats;
    }
}
