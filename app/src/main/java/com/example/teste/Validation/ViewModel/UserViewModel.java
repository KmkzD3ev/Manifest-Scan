package com.example.teste.Validation.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.teste.Validation.Model.UserResponse;
import com.example.teste.Validation.api.UserRepository;

public class UserViewModel extends ViewModel {
    private MutableLiveData<UserResponse> userResponseLiveData;
    private UserRepository userRepository;

    public UserViewModel() {
        userResponseLiveData = new MutableLiveData<>();
        userRepository = new UserRepository();
    }

    public LiveData<UserResponse> getUserResponseLiveData() {
        return userResponseLiveData;
    }

    public void authenticateUser(String id, String phoneNumber) {
        userRepository.authenticateUser(id, phoneNumber, userResponseLiveData);
    }
}
