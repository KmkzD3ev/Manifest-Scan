package br.com.zenitech.emissormdfe.Validation.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import br.com.zenitech.emissormdfe.Bd.DatabaseHelper;
import br.com.zenitech.emissormdfe.MainActivity;
import br.com.zenitech.emissormdfe.Validation.ViewModel.UserViewModel;
import br.com.zenitech.emissormdfe.R;

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

        Log.d("ActivityVal", "Codigo de validacao recebido da Api: " + codigoValidacao);

        EditText codeEditText = findViewById(R.id.checkcode);
        Button confirmButton = findViewById(R.id.confirmbtn);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        confirmButton.setOnClickListener(view -> {
            String code = codeEditText.getText().toString();

            if (codigoValidacao.equals(code)) {
                Log.d("ActivityVal", "Código de validação correto");

                databaseHelper.setLoggedIn(userId, true);
                Toast.makeText(ActivityVal.this, "Code confirmado com sucesso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivityVal.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.d("ActivityVal", "Código de validação incorreto, chamando API para validação");

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
            }
        });
    }
}
