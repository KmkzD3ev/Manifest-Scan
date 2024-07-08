package br.com.zenitech.emissormdfe.Validation.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import br.com.zenitech.emissormdfe.Validation.Model.UserResponse;
import br.com.zenitech.emissormdfe.Validation.api.UserRepository;
import android.util.Log;

public class UserViewModel extends ViewModel {
    private MutableLiveData<UserResponse> userResponseLiveData;
    private MutableLiveData<Boolean> validationResult;
    private UserRepository userRepository;

    public UserViewModel() {
        userResponseLiveData = new MutableLiveData<>();
        validationResult = new MutableLiveData<>();
        userRepository = new UserRepository();
    }

    public LiveData<UserResponse> getUserResponseLiveData() {
        return userResponseLiveData;
    }

    public LiveData<Boolean> getValidationResult() {
        return validationResult;
    }

    public void authenticateUser(String id, String phoneNumber) {
        Log.d("UserViewModel", "Iniciando autenticação para ID: " + id + " e Telefone: " + phoneNumber);
        userRepository.authenticateUser(id, phoneNumber, userResponseLiveData);
    }

    public void validateCode(String username, String code) {
        String storedValidationCode = userRepository.getStoredValidationCode(username);
        validationResult.setValue(code.equals(storedValidationCode));
    }
}
