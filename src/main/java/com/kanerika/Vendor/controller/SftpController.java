package com.kanerika.Vendor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
import com.kanerika.Vendor.dto.*;
import com.kanerika.Vendor.util.SftpUrlParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class SftpController {

    @GetMapping("/read-file")
    public ResponseEntity<?> readFile(@RequestParam("filePath") String fullUrl) {
        SftpUrlParser.ParsedSftpUrl parsed = SftpUrlParser.parse(fullUrl);

        String sftpHost = parsed.host;
        int sftpPort = 22;
        String sftpUser = parsed.username;
        String sftpPassword = "sftpuser"; // default or fetched from secure source
        String filePath = parsed.filePath;

        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(sftpUser, sftpHost, sftpPort);
            session.setPassword(sftpPassword);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            // Get file stats
            SftpATTRS attr = channelSftp.lstat(filePath);
            FileStatDTO fileStatDTO = new FileStatDTO();
            fileStatDTO.setSize(attr.getSize());
            fileStatDTO.setPermissions(Integer.toOctalString(attr.getPermissions()));
            fileStatDTO.setOwnerId(attr.getUId());
            fileStatDTO.setGroupId(attr.getGId());
            fileStatDTO.setLastModifiedTime(attr.getMTime());

            FileReadResponseDTO responseDTO = new FileReadResponseDTO();
            responseDTO.setFileStat(fileStatDTO);

            // Read file content
            InputStream inputStream = channelSftp.get(filePath);
            if (filePath.endsWith(".csv")) {
                List<Employee> employees = new ArrayList<>();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                boolean isFirstLine = true;

                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }
                    String[] parts = line.split(",");
                    if (parts.length == 5) {
                        Employee emp = new Employee(
                                Integer.parseInt(parts[0]),
                                parts[1],
                                Integer.parseInt(parts[2]),
                                parts[3],
                                Double.parseDouble(parts[4])
                        );
                        employees.add(emp);
                    }
                }

                reader.close();
                responseDTO.setEmployees(employees);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);

            } else if (filePath.endsWith(".json")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String json = reader.lines().reduce("", String::concat);
                reader.close();

                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> parsedJson = objectMapper.readValue(json, Map.class);
                responseDTO.setJsonContent(parsedJson);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unsupported file type", HttpStatus.BAD_REQUEST);
            }

        } catch (JSchException | SftpException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("File read failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if (channelSftp != null) channelSftp.disconnect();
            if (session != null) session.disconnect();
        }
    }

    @GetMapping("/readSubDirectoriesStats")
    public ResponseEntity<?> listSubdirectoriesOnly(@RequestParam("filePath") String directoryPath) {

        SftpUrlParser.ParsedSftpUrl parsed = SftpUrlParser.parse(directoryPath);

        String sftpHost = parsed.host;
        int sftpPort = 22;
        String sftpUser = parsed.username;
        String sftpPassword = "sftpuser"; // ideally should come from config or vault
        String path = parsed.filePath;

        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(sftpUser, sftpHost, sftpPort);
            session.setPassword(sftpPassword);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            Vector<ChannelSftp.LsEntry> entries = channelSftp.ls(path);

            List<FileOrDirectoryDTO> resultList = new ArrayList<>();

            for (ChannelSftp.LsEntry entry : entries) {
                String name = entry.getFilename();
                if (name.equals(".") || name.equals("..")) continue;

                SftpATTRS attrs = entry.getAttrs();

                FileStatDTO stat = new FileStatDTO();
                stat.setSize(attrs.getSize());
                stat.setPermissions(Integer.toOctalString(attrs.getPermissions()));
                stat.setOwnerId(attrs.getUId());
                stat.setGroupId(attrs.getGId());
                stat.setLastModifiedTime(attrs.getMTime());

                FileOrDirectoryDTO fileOrDir = new FileOrDirectoryDTO();
                fileOrDir.setName(name);
                fileOrDir.setType(attrs.isDir() ? "directory" : "file");
                fileOrDir.setStat(stat);

                resultList.add(fileOrDir);
            }

            return new ResponseEntity<>(resultList, HttpStatus.OK);

        } catch (JSchException | SftpException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to read directory: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if (channelSftp != null) channelSftp.disconnect();
            if (session != null) session.disconnect();
        }
    }
    @PostMapping(value = "/uploadFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFileToSftp(
            @RequestParam("filePath") String directoryPath,
            @RequestParam("file") MultipartFile file) {

        SftpUrlParser.ParsedSftpUrl parsed = SftpUrlParser.parse(directoryPath);

        String sftpHost = parsed.host;
        int sftpPort = 22;
        String sftpUser = parsed.username;
        String sftpPassword = "sftpuser";
        String path = parsed.filePath;

        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(sftpUser, sftpHost, sftpPort);
            session.setPassword(sftpPassword);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            try {
                channelSftp.cd(path);
            } catch (SftpException e) {
                channelSftp.mkdir(path);
                channelSftp.cd(path);
            }

            // Upload the file

            String fileName = file.getOriginalFilename();
            channelSftp.put(file.getInputStream(), fileName);

            return new ResponseEntity<>("File uploaded successfully.", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if (channelSftp != null) channelSftp.disconnect();
            if (session != null) session.disconnect();
        }
    }



}



