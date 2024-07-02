package com.example.teste;

import android.content.Intent;
import android.os.Build;

import androidx.lifecycle.MutableLiveData;

import com.example.teste.Bd.DatabaseHelper;
import com.example.teste.MainActivity;
import com.example.teste.Validation.Model.UserResponse;
import com.example.teste.Validation.ViewModel.UserViewModel;
import com.example.teste.Validation.views.Signin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1}, manifest = Config.NONE)

public class SigninUnitTest {
    @Mock
    private UserViewModel userViewModel;
    @Mock
    private DatabaseHelper databaseHelper;
    private MutableLiveData<UserResponse> userResponseLiveData;
    private Signin activity;
    private ActivityController<Signin> controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userResponseLiveData = new MutableLiveData<>();
        when(userViewModel.getUserResponseLiveData()).thenReturn(userResponseLiveData);
        controller = Robolectric.buildActivity(Signin.class);
        activity = controller.create().get();
        activity.userViewModel = userViewModel;
        activity.databaseHelper = databaseHelper;
    }

    @Test
    public void testLoginValidation() {
        UserResponse mockResponse = new UserResponse();
        mockResponse.setStatus("SUCCESS");
        mockResponse.setCodigoValidacao(Integer.parseInt("1234"));

        userResponseLiveData.postValue(mockResponse);

        // Simula o usuário clicando no botão de login
        activity.findViewById(R.id.btlogar).performClick();

        // Verifica se a atividade foi encerrada e a próxima foi iniciada corretamente
        assertTrue(activity.isFinishing());
        Intent expectedIntent = new Intent(activity, MainActivity.class);
        assertEquals(expectedIntent.getComponent(), Shadows.shadowOf(activity).getNextStartedActivity().getComponent());

    }
}