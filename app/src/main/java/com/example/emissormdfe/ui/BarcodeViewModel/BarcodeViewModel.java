package com.example.emissormdfe.ui.BarcodeViewModel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel para gerenciar o estado dos códigos de barras.
 */
public class BarcodeViewModel extends ViewModel {
    private MutableLiveData<String> firstBarcodeLiveData = new MutableLiveData<>();
    private MutableLiveData<String> secondBarcodeLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isContingency = new MutableLiveData<>();
    private MutableLiveData<String> barcodeLiveData = new MutableLiveData<>(); // Código de barras genérico

    /**
     * Define o primeiro código de barras.
     */
    public void setFirstBarcode(String barcode) {
        firstBarcodeLiveData.setValue(barcode);
    }

    /**
     * Retorna o primeiro código de barras.
     */
    public LiveData<String> getFirstBarcode() {
        return firstBarcodeLiveData;
    }

    /**
     * Define o segundo código de barras.
     */
    public void setSecondBarcode(String barcode) {
        secondBarcodeLiveData.setValue(barcode);
    }

    /**
     * Retorna o segundo código de barras.
     */
    public LiveData<String> getSecondBarcode() {
        return secondBarcodeLiveData;
    }

    /**
     * Define um código de barras genérico.
     */
    public void setBarcode(String barcode) {
        barcodeLiveData.setValue(barcode);
    }

    /**
     * Retorna o código de barras genérico.
     */
    public LiveData<String> getBarcode() {
        return barcodeLiveData;
    }

    /**
     * Define o estado de contingência.
     */
    public void setIsContingency(boolean isContingency) {
        this.isContingency.setValue(isContingency);
        Log.d("BarcodeViewModel", "isContingency set to: " + isContingency);
    }

    /**
     * Retorna o estado de contingência.
     */
    public LiveData<Boolean> getIsContingency() {
        return isContingency;
    }

    /**
     * Reseta todos os códigos de barras.
     */
    public void resetBarcodes() {
        firstBarcodeLiveData.setValue(null);
        secondBarcodeLiveData.setValue(null);
        barcodeLiveData.setValue(null);
    }
}
