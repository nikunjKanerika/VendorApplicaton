package com.kanerika.Vendor.util;

import java.net.URI;

public class SftpUrlParser {

    public static class ParsedSftpUrl {
        public String protocol;
        public String username;
        public String host;
        public String filePath;

        @Override
        public String toString() {
            return "ParsedSftpUrl{" +
                    "protocol='" + protocol + '\'' +
                    ", username='" + username + '\'' +
                    ", host='" + host + '\'' +
                    ", filePath='" + filePath + '\'' +
                    '}';
        }
    }

    public static ParsedSftpUrl parse(String url) {
        try {
            URI uri = new URI(url);

            ParsedSftpUrl result = new ParsedSftpUrl();
            result.protocol = uri.getScheme();
            result.host = uri.getHost();
            result.filePath = uri.getPath();

            String userInfo = uri.getUserInfo();
            if (userInfo != null) {
                result.username = userInfo.split(":")[0]; // optional: extend to handle passwords
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Invalid SFTP URL: " + url, e);
        }
    }
}
