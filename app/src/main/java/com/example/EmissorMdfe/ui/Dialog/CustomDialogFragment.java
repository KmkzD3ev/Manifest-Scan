package com.example.EmissorMdfe.ui.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.EmissorMdfe.R;
import com.example.EmissorMdfe.ui.BarcodeViewModel.BarcodeViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;



public class CustomDialogFragment extends DialogFragment {

    private TextInputEditText etWeight;
    private TextInputEditText etValue;
    private EditText etCodeNt;
    private TextView conttext;
    private EditText etChaveContingencia;
    private BarcodeViewModel barcodeViewModel;

    private OnDialogResult dialogResult;
    private static final String ARG_BARCODE = "barcode";
    private static final String ARG_IS_CONTINGENCY = "is_contingency";
    private boolean secondScanComplete = false;  // Flag to track second scan

    public interface OnDialogResult {
        void finish(String barcode, String weight, String value, String chaveContingencia, boolean isContingency);
    }

    public void setDialogResult(OnDialogResult dialogResult) {
        this.dialogResult = dialogResult;
    }

    public static CustomDialogFragment newInstance(String barcode, boolean isContingency) {
        CustomDialogFragment fragment = new CustomDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BARCODE, barcode);
        args.putBoolean(ARG_IS_CONTINGENCY, isContingency);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        barcodeViewModel = new ViewModelProvider(requireActivity()).get(BarcodeViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Informações Adicionais");

        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_layout, null);
        builder.setView(view);

        etCodeNt = view.findViewById(R.id.etCodeNt);
        etWeight = view.findViewById(R.id.input_ps);
        etValue = view.findViewById(R.id.input_pr);
        etChaveContingencia = view.findViewById(R.id.etChaveContingencia);
        conttext = view.findViewById(R.id.conttext);

// TextWatcher para o campo de peso
        TextWatcher weightTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String originalText = s.toString();
                String updatedText = originalText.replace('.', ',');
                if (!originalText.equals(updatedText)) {
                    etWeight.removeTextChangedListener(this);
                    etWeight.setText(updatedText);
                    etWeight.setSelection(updatedText.length());
                    etWeight.addTextChangedListener(this);
                }
            }
        };

// TextWatcher para o campo de valor
        TextWatcher valueTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String originalText = s.toString();
                String updatedText = originalText.replace('.', ',');
                if (!originalText.equals(updatedText)) {
                    etValue.removeTextChangedListener(this);
                    etValue.setText(updatedText);
                    etValue.setSelection(updatedText.length());
                    etValue.addTextChangedListener(this);
                }
            }
        };

// Aplicando TextWatcher específico para cada EditText
        etWeight.addTextChangedListener(weightTextWatcher);
        etValue.addTextChangedListener(valueTextWatcher);

        // Obter os dados do ViewModel
        String firstBarcode = barcodeViewModel.getFirstBarcode().getValue();
        String secondBarcode = barcodeViewModel.getSecondBarcode().getValue();
        boolean isContingency = firstBarcode != null;
        String currentBarcode = barcodeViewModel.getBarcode().getValue();

        // Log para verificar como os dados são recebidos no diálogo
        Log.d("CustomDialogFragment", "Dados recebidos: First Barcode: " + firstBarcode +
                ", Second Barcode: " + secondBarcode + ", Is Contingency: " + isContingency + ", Current Barcode: " + currentBarcode);

        if (etCodeNt != null) {
            if (isContingency) {
                etCodeNt.setText(firstBarcode != null ? firstBarcode : secondBarcode);
            } else {
                etCodeNt.setText(currentBarcode);
            }
        }

        if (etWeight != null) {
            etWeight.setVisibility(View.VISIBLE); // Garante que o campo de peso esteja sempre visível.
        }

        if (etValue != null) {
            etValue.setVisibility(View.VISIBLE); // Garante que o campo de valor esteja sempre visível.
        }

        builder.setPositiveButton("Salvar", null); // Inicializa sem listener para definir posteriormente

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        Dialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String weight = etWeight != null ? etWeight.getText().toString() : "";
                        String value = etValue != null ? etValue.getText().toString() : "";
                        String chaveContingencia = etChaveContingencia != null ? etChaveContingencia.getText().toString() : "";

                        // Atualiza o código de barras atual baseado no estado da contingência
                        String barcodeToSave = isContingency ? firstBarcode : currentBarcode;

                        if (isContingency && !secondScanComplete) {
                            // Mostra um alerta se for de contingência e o segundo scan não está completo
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Nota de Contingência")
                                    .setMessage("Esta nota é de contingência. Escaneie o código de barras da chave de contingência.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Inicia um novo escaneamento para a chave de contingência
                                            IntentIntegrator.forSupportFragment(CustomDialogFragment.this).initiateScan();
                                            secondScanComplete = true;  // Marca que o segundo scan foi completado
                                        }
                                    })
                                    .show();
                        } else {
                            // Salva diretamente se não for de contingência ou se o segundo scan foi completado
                            if (dialogResult != null) {
                                dialogResult.finish(barcodeToSave, weight, value, chaveContingencia, isContingency);
                                Log.d("CustomDialogFragment", "Dados salvos: Barcode: " + barcodeToSave +
                                        ", Weight: " + weight + ", Value: " + value + ", Chave Contingência: " + chaveContingencia);
                            }
                            dialog.dismiss();
                            resetDialogState(); // Resetar o estado do diálogo após o salvamento
                        }
                    }
                });
            }
        });

        return dialog;
    }

    // Método para processar o resultado do escaneamento de código de barras
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String scannedCode = result.getContents();
            // Define a lógica para usar o código escaneado como a chave de contingência
            if (etChaveContingencia != null) {
                etChaveContingencia.setText(scannedCode);
                etChaveContingencia.setVisibility(View.VISIBLE);
                // Marca que o segundo scan foi completado
                secondScanComplete = true;
            }
        }
    }

    // Método para resetar o estado do diálogo
    private void resetDialogState() {
        etWeight.setText("");
        etValue.setText("");
        etChaveContingencia.setText("");
        etCodeNt.setText("");
        secondScanComplete = false;
        barcodeViewModel.resetBarcodes();
    }
}