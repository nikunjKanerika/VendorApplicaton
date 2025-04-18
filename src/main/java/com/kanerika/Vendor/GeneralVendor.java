package com.kanerika.Vendor;

import com.kanerika.Vendor.dto.VendorRequest;

public interface GeneralVendor {
    String connect(VendorRequest request);

    String connect(String host , String port, String username,String password);

    String connect(String s, String s1);

    String executeQuery(String query);

    String executeUnloadData(String query, String format, String location);
}

