package com.kanerika.Vendor.dto;

import java.util.List;
import java.util.Map;

public class FileReadResponseDTO {
    private FileStatDTO fileStat;
    private List<Employee> employees; // for CSV
    private Map<String, Object>jsonContent;// for JSON
    private String directoryPath;
    private List<FileStatDTO> fileList;

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public List<FileStatDTO> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileStatDTO> fileList) {
        this.fileList = fileList;
    }

    public FileStatDTO getFileStat() {
        return fileStat;
    }

    public void setFileStat(FileStatDTO fileStat) {
        this.fileStat = fileStat;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }


    public Map<String, Object> getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(Map<String, Object> jsonContent) {
        this.jsonContent = jsonContent;
    }
}
