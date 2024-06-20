package com.example.teste.ui.BarcodeViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BarcodeViewModel extends ViewModel {
    private MutableLiveData<String> barcodeLiveData = new MutableLiveData<>();

    public void setBarcode(String barcode) {
        barcodeLiveData.setValue(barcode);
    }

    public LiveData<String> getBarcode() {
        return barcodeLiveData;
    }
}
