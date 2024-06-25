package com.example.teste.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teste.Adapter.NotaAdapter;

import com.example.teste.Bd.DatabaseHelper;
import com.example.teste.R;
import com.example.teste.Validation.Model.Nota;
import com.example.teste.databinding.FragmentGalleryBinding;
import com.example.teste.ui.BarcodeViewModel.BarcodeViewModel;
import com.example.teste.ui.Dialog.CustomDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private NotaAdapter notaAdapter;
    private GalleryViewModel galleryViewModel;
    private BarcodeViewModel barcodeViewModel;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        barcodeViewModel = new ViewModelProvider(requireActivity()).get(BarcodeViewModel.class);
        databaseHelper = new DatabaseHelper(getContext());

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button btnScan = root.findViewById(R.id.btn_scan);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);

        notaAdapter = new NotaAdapter(getContext(), new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(notaAdapter);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(GalleryFragment.this).initiateScan();
            }
        });

        FloatingActionButton fabAdditionalAction = root.findViewById(R.id.fab_additional_action);
        fabAdditionalAction.setOnClickListener(v -> {
            // Adicione aqui a ação que deve ser realizada quando o FloatingActionButton é pressionado
        });

        galleryViewModel.getNotas().observe(getViewLifecycleOwner(), notas -> {
            notaAdapter.setNotas(notas);
            notaAdapter.notifyDataSetChanged();
            updateButtonVisibility();
        });



        barcodeViewModel.getIsContingency().observe(getViewLifecycleOwner(), isContingency -> {
            Log.d("GalleryFragment", "Observed isContingency: " + isContingency);
            if (isContingency) {
                // Ações específicas para códigos de contingência
            } else {
                // Ações para códigos normais
            }
        });

        return root;
    }

    private void updateButtonVisibility() {
        FloatingActionButton fabAdditionalAction = binding.fabAdditionalAction;
        if (notaAdapter.getItemCount() > 0) {
            fabAdditionalAction.show();
        } else {
            fabAdditionalAction.hide();
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String barcodeResult = result.getContents();
            Log.d("Passando codigo que foi lido", "Scanned Code: " + barcodeResult);
            boolean isContingency = barcodeResult.length() >= 44 && barcodeResult.charAt(34) != '1';

            barcodeViewModel.setIsContingency(isContingency);
            Log.d("GalleryFragment", "Scanned barcode isContingency: " + isContingency);

            if (isContingency) {
                if (barcodeViewModel.getFirstBarcode().getValue() == null) {
                    barcodeViewModel.setFirstBarcode(barcodeResult);
                    Log.d("Passando codigo que foi lido", "ENVIANDO AO VIEWMODEL CONT: " + barcodeResult);
                    openCustomDialog(barcodeResult, isContingency);
                } else {
                    barcodeViewModel.setSecondBarcode(barcodeResult);
                    Log.d("Passando codigo que foi lido", "ENVIANDO AO VIEWMODEL SEGUNDO CONT: " + barcodeResult);
                    openCustomDialog(barcodeResult, isContingency);
                }
            } else {
                barcodeViewModel.setBarcode(barcodeResult);
                Log.d("Passando codigo que foi lido", "ENVIANDO AO VIEWMODEL NORMAL: " + barcodeResult);
                openCustomDialog(barcodeResult, isContingency);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void openCustomDialog(String barcodeResult, boolean isContingency) {
        Log.d("GalleryFragment", "Abrindo dialog com barcode: " + barcodeResult + " Is Contingency: " + isContingency);
        CustomDialogFragment dialogFragment = CustomDialogFragment.newInstance(barcodeResult, isContingency);
        dialogFragment.setDialogResult(new CustomDialogFragment.OnDialogResult() {
            @Override
            public void finish(String barcode, String weight, String value, String chaveContingencia, boolean isContingency) {
                Log.d("GalleryFragment", "Dados recebidos do dialog: Barcode: " + barcode +
                        ", Weight: " + weight + ", Value: " + value + ", Chave Contingência: " + chaveContingencia);


                // Criar uma nova Nota e adicionar ao ViewModel
                Nota nota;
                if (isContingency) {
                    // Usar o construtor para contingência
                    nota = new Nota(barcode, weight, value, chaveContingencia);
                } else {
                    // Usar o construtor padrão
                    nota = new Nota(barcode, weight, value);
                }
                galleryViewModel.addNota(nota);
                databaseHelper.insertNota(nota);
                updateButtonVisibility();

                Snackbar.make(binding.getRoot(), "Nota salva com sucesso!", Snackbar.LENGTH_LONG).show();
            }
        });
        dialogFragment.show(getChildFragmentManager(), "custom_dialog");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}