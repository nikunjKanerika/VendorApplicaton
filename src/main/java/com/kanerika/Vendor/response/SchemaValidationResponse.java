package com.kanerika.Vendor.response;

import java.util.ArrayList;
import java.util.List;

public class SchemaValidationResponse {
    private int matched;
    private List<String> mismatchDetails;


    public SchemaValidationResponse(int matched, List<String> mismatchDetails) {
        this.matched = matched;
        this.mismatchDetails = mismatchDetails;
    }

    public int getMatched() {
        return matched;
    }

    public void setMatched(int matched) {
        this.matched = matched;
    }

    public List<String> getMismatchDetails() {
        return mismatchDetails;
    }

    public void setMismatchDetails(List<String> mismatchDetails) {
        this.mismatchDetails = mismatchDetails;
    }
}
