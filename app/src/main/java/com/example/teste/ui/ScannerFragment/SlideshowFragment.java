package com.example.teste.ui.ScannerFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.teste.ApiService.ApiTest;
import com.example.teste.Bd.DatabaseHelper;
import com.example.teste.Validation.Model.User;
import com.example.teste.R;
import com.example.teste.databinding.FragmentSlideshowBinding;
import com.example.teste.ui.BarcodeViewModel.BarcodeViewModel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private TextView tvBarcodeResult;
    private String barcodeResult;
    private DatabaseHelper databaseHelper;
    private BarcodeViewModel barcodeViewModel;

    private boolean isManifestoAuthorized = false;
    private String apiMessage = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tvBarcodeResult = root.findViewById(R.id.tv_barcode_result);
        Button btnScan = root.findViewById(R.id.btn_scan);
        Button btnSend = root.findViewById(R.id.btn_send);
        databaseHelper = new DatabaseHelper(getContext());
        barcodeViewModel = new ViewModelProvider(requireActivity()).get(BarcodeViewModel.class);

        btnScan.setOnClickListener(v -> {
            Log.i("SlideshowFragment", "Iniciando escaneamento de código de barras.");
            IntentIntegrator.forSupportFragment(SlideshowFragment.this).initiateScan();
        });

        btnSend.setOnClickListener(v -> {
            if (isManifestoAuthorized) {
                Log.i("SlideshowFragment", "Navegando para a galeria.");
                NavHostFragment.findNavController(this).navigate(R.id.nav_gallery);
            } else {
                Toast.makeText(getContext(), apiMessage.isEmpty() ? "Manifesto não autorizado" : apiMessage, Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            barcodeResult = result.getContents();
            Log.i("SlideshowFragment", "Código de barras escaneado: " + barcodeResult);
            tvBarcodeResult.setText(barcodeResult);
            showDialogWithBarcode(barcodeResult);
            saveManifestoToDatabase(barcodeResult);
            barcodeViewModel.setBarcode(barcodeResult);

            User user = databaseHelper.getUserDetails();
            if (user != null) {
                // Adicionar logs para verificar valores individuais
                Log.i("SlideshowFragment", "User ID: " + user.getId());
                Log.i("SlideshowFragment", "Phone Number: " + user.getPhoneNumber());
                Log.i("SlideshowFragment", "Barcode Result: " + barcodeResult);

                ApiTest.testApiCall(getContext(), user.getId(), user.getPhoneNumber(), barcodeResult, new ApiTest.ApiCallback() {
                    @Override
                    public void onResponse(boolean authorized, String message) {
                        isManifestoAuthorized = authorized;
                        apiMessage = message;
                        // Exibir a mensagem da API em um Toast
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                });
                Log.i("SlideshowFragment", "Chamando a API com dados dinâmicos.");
            } else {
                Toast.makeText(getContext(), "Detalhes do usuário não encontrados.", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showDialogWithBarcode(String barcode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Código de Barras Lido");
        builder.setMessage(barcode);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.create().show();
        Log.i("SlideshowFragment", "Diálogo exibido com o código de barras.");
    }

    private void saveManifestoToDatabase(String barcode) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        long id = databaseHelper.insertManifesto(barcode, timestamp);
        if (id != -1) {
            Toast.makeText(getContext(), "Código de manifesto salvo com sucesso!", Toast.LENGTH_SHORT).show();
            Log.i("SlideshowFragment", "Código de manifesto salvo. ID: " + id);
        } else {
            Toast.makeText(getContext(), "Falha ao salvar o código de manifesto.", Toast.LENGTH_SHORT).show();
            Log.e("SlideshowFragment", "Falha ao salvar o código de manifesto.");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
