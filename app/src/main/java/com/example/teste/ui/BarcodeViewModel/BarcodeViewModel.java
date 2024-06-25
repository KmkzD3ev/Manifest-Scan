package com.example.teste.ui.BarcodeViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class BarcodeViewModel extends ViewModel {
    private MutableLiveData<String> firstBarcodeLiveData = new MutableLiveData<>();
    private MutableLiveData<String> secondBarcodeLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isContingency = new MutableLiveData<>();

    private MutableLiveData<String> barcodeLiveData = new MutableLiveData<>(); // Adicionado para código de barras genérico

    public void setFirstBarcode(String barcode) {
        firstBarcodeLiveData.setValue(barcode);
    }

    public LiveData<String> getFirstBarcode() {
        return firstBarcodeLiveData;
    }

    public void setSecondBarcode(String barcode) {
        secondBarcodeLiveData.setValue(barcode);
    }

    public LiveData<String> getSecondBarcode() {
        return secondBarcodeLiveData;
    }
    public void setBarcode(String barcode) {
        barcodeLiveData.setValue(barcode);
    }
    public LiveData<String> getBarcode() {
        return barcodeLiveData;
    }

    public void setIsContingency(boolean isContingency) {
        this.isContingency.setValue(isContingency);
        Log.d("BarcodeViewModel", "isContingency set to: " + isContingency);
    }

    public LiveData<Boolean> getIsContingency() {
        return isContingency;
    }

    public void resetBarcodes() {
        firstBarcodeLiveData.setValue(null);
        secondBarcodeLiveData.setValue(null);
        barcodeLiveData.setValue(null);
    }

}

