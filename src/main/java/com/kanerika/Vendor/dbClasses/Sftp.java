package com.kanerika.Vendor.dbClasses;

import com.kanerika.Vendor.GeneralVendor;
import com.kanerika.Vendor.dto.VendorRequest;
import org.springframework.stereotype.Component;

@Component
public class Sftp implements GeneralVendor {
    @Override
    public String connect(VendorRequest request) {
        return "";
    }

    @Override
    public String connect(String host, String port, String username, String password) {
        return "";
    }

    @Override
    public String connect(String s, String s1) {
        return "";
    }

    @Override
    public String executeQuery(String query) {
        return "";
    }

    @Override
    public String executeUnloadData(String query, String format, String location) {
        return "";
    }
}
