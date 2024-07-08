package com.example.emissormdfe.Validation.Model;
public class BarcodeData {
    private String barcode;
    private boolean isContingency;

    public BarcodeData(String barcode, boolean isContingency) {
        this.barcode = barcode;
        this.isContingency = isContingency;
    }

    public String getBarcode() {
        return barcode;
    }

    public boolean isContingency() {
        return isContingency;
    }
}
