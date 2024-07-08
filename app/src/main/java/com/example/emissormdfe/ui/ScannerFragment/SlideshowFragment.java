package com.example.emissormdfe.ui.ScannerFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.emissormdfe.Bd.DatabaseHelper;

import com.example.emissormdfe.ui.Dialog.Dialogs;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.zenitech.br.com.zenitech.emissormdfe.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private SlideshowViewModel slideshowViewModel;
    private Dialogs dialogs;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla o layout para este fragmento
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializa o ViewModel e Dialogs
        slideshowViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(SlideshowViewModel.class);
        dialogs = new Dialogs(getActivity());

        // Inicializa o DatabaseHelper
        databaseHelper = new DatabaseHelper(getContext());

        // Observa mensagens da API e exibe um Toast
        slideshowViewModel.getApiMessage().observe(getViewLifecycleOwner(), message -> {
            if (!message.isEmpty()) {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });

        // Observa se o manifesto foi autorizado e navega para a galeria após um atraso
        slideshowViewModel.getIsManifestoAuthorized().observe(getViewLifecycleOwner(), authorized -> {
            if (authorized) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (isAdded()) {
                        dialogs.liberarDialog();
                        NavHostFragment.findNavController(SlideshowFragment.this).navigate(br.com.zenitech.br.com.zenitech.emissormdfe.R.id.nav_gallery);
                    } else {
                        Log.e("SlideshowFragment", "Fragment not associated with FragmentManager");
                    }
                }, 3000); // Delay para simular carregamento
            } else {
                dialogs.liberarDialog();
            }
        });

        // Configura o botão de scan para iniciar a leitura do código de barras
        binding.btnScan.setOnClickListener(v -> {
            IntentIntegrator.forSupportFragment(this).initiateScan();
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            // Lida com o resultado do scan de código de barras
            String scannedBarcode = result.getContents();
            Log.d("SlideshowFragment", "Scanned Code: " + scannedBarcode);

            // Inicia o diálogo de carregamento
            dialogs.iniciarDialog();

            // Obtém o timestamp atual
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            // Salva o manifesto no banco de dados local
            long manifestoId = databaseHelper.insertManifesto(scannedBarcode, timestamp);
            if (manifestoId != -1) {
                Toast.makeText(getContext(), "Manifesto salvo com sucesso!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Erro ao salvar manifesto!", Toast.LENGTH_LONG).show();
            }

            // Limpa as notas do usuário atual
            int userId = databaseHelper.getLoggedInUserId();
            if (userId != -1) {
                databaseHelper.clearNotas(userId);
            }

            // Lida com o resultado do scan no ViewModel
            slideshowViewModel.handleBarcodeResult(scannedBarcode);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
