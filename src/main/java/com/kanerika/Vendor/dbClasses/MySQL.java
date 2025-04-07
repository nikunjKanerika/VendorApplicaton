package com.kanerika.Vendor.dbClasses;

import com.kanerika.Vendor.GeneralVendor;

public class MySQL implements GeneralVendor {
    @Override
    public String connect() {
        return "Connected to MongoDB";
    }
}
