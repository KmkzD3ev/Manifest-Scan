package com.example.teste.ui.ScannerFragment;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.teste.ApiService.ApiService;
import com.example.teste.ApiService.RetrofitClient;
import com.example.teste.ApiService.ServerResponse;
import com.example.teste.Bd.DatabaseHelper;
import com.example.teste.R;
import com.example.teste.databinding.FragmentSlideshowBinding;
import com.example.teste.ui.BarcodeViewModel.BarcodeViewModel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private TextView tvBarcodeResult;
    private String barcodeResult;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tvBarcodeResult = root.findViewById(R.id.tv_barcode_result);
        Button btnScan = root.findViewById(R.id.btn_scan);
        Button btnSend = root.findViewById(R.id.btn_send);

        // Inicializa o DatabaseHelper
        databaseHelper = new DatabaseHelper(getContext());

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(SlideshowFragment.this).initiateScan();
            }
        });
        btnSend.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.nav_gallery);
        });


        return root;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                tvBarcodeResult.setText("Cancelado");
                Toast.makeText(getContext(), "Leitura do código de barras cancelada", Toast.LENGTH_SHORT).show();
            } else {
                barcodeResult = result.getContents();
                showDialogWithBarcode(barcodeResult);;
                saveBarcodeToDatabase(barcodeResult);
                BarcodeViewModel viewModel = new ViewModelProvider(requireActivity()).get(BarcodeViewModel.class);
                viewModel.setBarcode(barcodeResult);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void showDialogWithBarcode(String barcode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Código de Barras Lido");
        builder.setMessage(barcode);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Fecha o diálogo
            }
        });
        builder.create().show();
    }

    private void saveBarcodeToDatabase(String barcode) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        long id = databaseHelper.insertBarcode(barcode, timestamp);

        if (id != -1) {
            Toast.makeText(getContext(), "Código de barras salvo com sucesso!", Toast.LENGTH_SHORT).show();
            Log.i("DATABASE", "Código de barras salvo com sucesso. ID: " + id);
        } else {
            Toast.makeText(getContext(), "Falha ao salvar o código de barras.", Toast.LENGTH_SHORT).show();
            Log.e("DATABASE", "Falha ao salvar o código de barras.");
        }
    }

    private void sendDataToServer() {
        if (barcodeResult != null) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
            Call<ServerResponse> call = apiService.sendBarcodeData(barcodeResult, timestamp);

            call.enqueue(new Callback<ServerResponse>() {
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    if (response.isSuccessful()) {
                        ServerResponse serverResponse = response.body();
                        if (serverResponse != null) {
                            Toast.makeText(getContext(), "Dados enviados com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Resposta inesperada do servidor", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Erro no Servidor", Toast.LENGTH_SHORT).show();
                    }
                }

                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Falha ao enviar dados: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("API_CALL", "Erro na chamada da API", t);
                }
            });
        } else {
            Toast.makeText(getContext(), "Nenhum código de barras para enviar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
