package com.example.teste.Validation.views;

import static com.example.teste.R.id.ediId;
import static com.example.teste.R.id.editphone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.teste.Bd.DatabaseHelper;
import com.example.teste.MainActivity;
import com.example.teste.R;

import com.example.teste.Validation.ViewModel.UserViewModel;
import com.google.android.material.textfield.TextInputEditText;







public class Signin extends AppCompatActivity {
    private UserViewModel userViewModel;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        databaseHelper = new DatabaseHelper(this);

        // Verificar se o usuário já está logado
        if (databaseHelper.isUserLoggedIn()) {
            Intent intent = new Intent(Signin.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.ciano));
        }

        TextInputEditText idEditText = findViewById(R.id.ediId);
        TextInputEditText phoneEditText = findViewById(R.id.editphone);
        Button buttonLogin = findViewById(R.id.btlogar);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = idEditText.getText().toString();
                String phoneNumber = phoneEditText.getText().toString();
                userViewModel.authenticateUser(id, phoneNumber);

                AlertDialog.Builder builder = new AlertDialog.Builder(Signin.this);
                builder.setView(R.layout.dialog_carregamento);
                AlertDialog loadingDialog = builder.create();
                loadingDialog.show();

                new AuthenticateUserTask(id, phoneNumber, loadingDialog).execute();
            }
        });
    }

    private class AuthenticateUserTask extends AsyncTask<Void, Void, Void> {
        private String id;
        private String phoneNumber;
        private AlertDialog loadingDialog;

        public AuthenticateUserTask(String id, String phoneNumber, AlertDialog loadingDialog) {
            this.id = id;
            this.phoneNumber = phoneNumber;
            this.loadingDialog = loadingDialog;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // Simular um atraso de 3 segundos
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadingDialog.dismiss();

            userViewModel.getUserResponseLiveData().observe(Signin.this, userResponse -> {
                if (userResponse != null) {
                    // Salvar usuário no banco de dados
                    databaseHelper.addUser(id, phoneNumber, userResponse.getCodigo_validacao());
                    databaseHelper.setLoggedIn(id, true);

                    // Iniciar a ActivityVal para a próxima etapa
                    Intent intent = new Intent(Signin.this, ActivityVal.class);
                    intent.putExtra("username", id);
                    intent.putExtra("codigo_validacao", String.valueOf(userResponse.getCodigo_validacao())); // Converta para String aqui
                    startActivity(intent);
                } else {
                    Toast.makeText(Signin.this, "ERROR: Id ou Telefone Invalido", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}