package com.example.teste.Validation.views;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.teste.Bd.DatabaseHelper;
import com.example.teste.MainActivity;
import com.example.teste.R;
import com.example.teste.Validation.ViewModel.UserViewModel;




public class ActivityVal extends AppCompatActivity {
    private UserViewModel userViewModel;
    private DatabaseHelper databaseHelper;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_val);

        databaseHelper = new DatabaseHelper(this);
        username = getIntent().getStringExtra("username");

        EditText codeEditText = findViewById(R.id.checkcode);
        Button confirmButton = findViewById(R.id.confirmbtn);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = codeEditText.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityVal.this);
                builder.setView(R.layout.dialog_carregamento);
                AlertDialog loadingDialog = builder.create();
                loadingDialog.show();

                new ValidateCodeTask(username, code, loadingDialog).execute();
            }
        });
    }

    private class ValidateCodeTask extends AsyncTask<Void, Void, Boolean> {
        private String username;
        private String code;
        private AlertDialog loadingDialog;

        public ValidateCodeTask(String username, String code, AlertDialog loadingDialog) {
            this.username = username;
            this.code = code;
            this.loadingDialog = loadingDialog;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                // Simular um atraso de 3 segundos
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String validationCode = databaseHelper.getValidationCode(username);
            return code.equals(validationCode);
        }

        @Override
        protected void onPostExecute(Boolean isValid) {
            loadingDialog.dismiss();

            if (isValid) {
                databaseHelper.setLoggedIn(username, true);

                Toast.makeText(ActivityVal.this, "Code confirmado com sucesso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivityVal.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(ActivityVal.this, "Código de validação incorreto", Toast.LENGTH_SHORT).show();
            }
        }
    }
}