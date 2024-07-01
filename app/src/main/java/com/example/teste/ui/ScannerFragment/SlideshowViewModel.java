package com.example.teste.ui.ScannerFragment;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.teste.ApiService.ApiTest;
import com.example.teste.Bd.DatabaseHelper;
import com.example.teste.Validation.Model.User;

public class SlideshowViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> isManifestoAuthorized = new MutableLiveData<>();
    private MutableLiveData<String> apiMessage = new MutableLiveData<>();
    private DatabaseHelper databaseHelper;

    public SlideshowViewModel(@NonNull Application application) {
        super(application);
        databaseHelper = new DatabaseHelper(application);
    }

    public LiveData<Boolean> getIsManifestoAuthorized() {
        return isManifestoAuthorized;
    }

    public LiveData<String> getApiMessage() {
        return apiMessage;
    }

    public void handleBarcodeResult(String barcode) {
        User user = databaseHelper.getUserDetails();
        if (user != null) {
            ApiTest.testApiCall(getApplication(), user.getId(), user.getPhoneNumber(), barcode, new ApiTest.ApiCallback() {
                @Override
                public void onResponse(boolean authorized, String message) {
                    Log.d("SlideshowViewModel", "API Response - Authorized: " + authorized + ", Message: " + message);
                    apiMessage.postValue(message);
                    isManifestoAuthorized.postValue(authorized);
                }
            });
        } else {
            apiMessage.postValue("Detalhes do usuário não encontrados.");
        }
    }
}
