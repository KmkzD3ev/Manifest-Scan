package com.example.EmissorMdfe.Validation.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.EmissorMdfe.Bd.DatabaseHelper;
import com.example.EmissorMdfe.MainActivity;
import com.example.EmissorMdfe.R;
import com.example.EmissorMdfe.Validation.ViewModel.UserViewModel;

public class ActivityVal extends AppCompatActivity {
    private UserViewModel userViewModel;
    private DatabaseHelper databaseHelper;
    private int userId;
    private String codigoValidacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_val);

        databaseHelper = new DatabaseHelper(this);
        userId = Integer.parseInt(getIntent().getStringExtra("id")); // Converte o ID para inteiro
        codigoValidacao = getIntent().getStringExtra("codigo_validacao");

        Log.d("ActivityVal", "Codigo de validacao recebido: " + codigoValidacao);

        EditText codeEditText = findViewById(R.id.checkcode);
        Button confirmButton = findViewById(R.id.confirmbtn);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        confirmButton.setOnClickListener(view -> {
            String code = codeEditText.getText().toString();

            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityVal.this);
            builder.setView(R.layout.dialog_carregamento);
            AlertDialog loadingDialog = builder.create();
            loadingDialog.show();

            userViewModel.validateCode(String.valueOf(userId), code);

            userViewModel.getValidationResult().observe(this, validationResult -> {
                loadingDialog.dismiss();
                if (validationResult != null && validationResult) {
                    databaseHelper.setLoggedIn(userId, true);
                    Toast.makeText(ActivityVal.this, "Code confirmado com sucesso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivityVal.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ActivityVal.this, "Código de validação incorreto", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
