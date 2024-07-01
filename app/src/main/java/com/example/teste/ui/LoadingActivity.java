package com.example.teste.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.teste.R;

public class LoadingActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView textViewStage;
    private Handler handler = new Handler();
    private int progressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progressBar = findViewById(R.id.progressBar);
        textViewStage = findViewById(R.id.textViewStage);
        progressBar.setMax(100); // Definir o máximo da ProgressBar

        Thread progressThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int[] stageIncrements = {33, 66, 100}; // Os pontos de progresso para cada etapa
                int delayBetweenStages = 2000; // 2 segundos de pausa entre cada estágio

                for (int targetProgress : stageIncrements) {
                    while (progressStatus < targetProgress) {
                        progressStatus++;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(progressStatus);
                                updateStageText(progressStatus);
                            }
                        });

                        try {
                            Thread.sleep(20); // Controla a velocidade de preenchimento
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // Pausa após alcançar cada estágio antes de começar o próximo
                    try {
                        Thread.sleep(delayBetweenStages);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        progressThread.start();
    }

    private void updateStageText(int progress) {
        if (progress < 33) {
            textViewStage.setText("Enviando seus Dados");
        } else if (progress < 66) {
            textViewStage.setText("Obtendo validação");
        } else if (progress < 100) {
            textViewStage.setText("Processando...");
        } else {
            progressBar.setVisibility(View.GONE); // Esconde a ProgressBar
            textViewStage.setVisibility(View.GONE); // Esconde o texto de estágio

            ImageView imageViewSmiley = findViewById(R.id.imageViewSmiley);
            TextView textViewCompletion = findViewById(R.id.textViewCompletion);

            imageViewSmiley.setVisibility(View.VISIBLE); // Mostra a carinha feliz
            textViewCompletion.setVisibility(View.VISIBLE); // Mostra o texto "Tudo OK!"
        }
    }
}
