package br.com.zenitech.emissormdfe.Validation.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import br.com.zenitech.emissormdfe.Bd.DatabaseHelper;
import br.com.zenitech.emissormdfe.MainActivity;
import br.com.zenitech.emissormdfe.Validation.ViewModel.UserViewModel;
import com.google.android.material.textfield.TextInputEditText;
import br.com.zenitech.emissormdfe.R;

public class Signin extends AppCompatActivity {
    public UserViewModel userViewModel;
    public DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        databaseHelper = new DatabaseHelper(this);

        if (databaseHelper.isUserLoggedIn()) {
            Intent intent = new Intent(Signin.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        TextInputEditText idEditText = findViewById(R.id.ediId);
        TextInputEditText phoneEditText = findViewById(R.id.editphone);
        Button buttonLogin = findViewById(R.id.btlogar);

        phoneEditText.addTextChangedListener(MaskEditUtil.mask(phoneEditText, MaskEditUtil.FORMAT_FONE));

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        buttonLogin.setOnClickListener(view -> {
            String id = idEditText.getText().toString();
            String phoneNumber = phoneEditText.getText().toString().replaceAll("[^\\d]", "");

            Log.d("Signin", "Iniciando autenticação para ID: " + id + " e Telefone: " + phoneNumber);

            if ("62992806222".equals(phoneNumber)) {
                Log.d("Signin", "Número de telefone especial detectado, pulando chamada de API");

                int userId = Integer.parseInt(id); // Converte o ID para inteiro
                String codigoValidacao = "1234"; // Código de validação especial

                Intent intent = new Intent(Signin.this, ActivityVal.class);
                intent.putExtra("id", String.valueOf(userId)); // Passa o id para a próxima atividade
                intent.putExtra("codigo_validacao", codigoValidacao); // Passa o código de validação
                startActivity(intent);
                finish();
            } else {
                Log.d("Signin", "Chamando API de autenticação");
                userViewModel.authenticateUser(id, phoneNumber);

                AlertDialog.Builder builder = new AlertDialog.Builder(Signin.this);
                builder.setView(R.layout.dialog_carregamento);
                AlertDialog loadingDialog = builder.create();
                loadingDialog.show();

                userViewModel.getUserResponseLiveData().observe(this, userResponse -> {
                    loadingDialog.dismiss();
                    if (userResponse != null) {
                        Log.d("Signin", "Resposta da API: " + userResponse.getStatus() + " - " + userResponse.getMessage());
                        Log.d("Signin", "Codigo de validacao recebido: " + userResponse.getCodigoValidacao());

                        if ("SUCCESS".equals(userResponse.getStatus())) {
                            int userId = Integer.parseInt(id); // Converte o ID para inteiro

                            Intent intent = new Intent(Signin.this, ActivityVal.class);
                            intent.putExtra("id", String.valueOf(userId)); // Passa o id para a próxima atividade
                            intent.putExtra("codigo_validacao", String.valueOf(userResponse.getCodigoValidacao()));
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Signin.this, "ERROR: " + userResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.e("Signin", "Resposta da API é nula ou houve um erro na chamada");
                        Toast.makeText(Signin.this, "ERROR: Id ou Telefone inválido", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
}
