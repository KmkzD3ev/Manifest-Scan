package com.example.teste.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teste.Adapter.NotaAdapter;
import com.example.teste.R;
import com.example.teste.Validation.Model.Nota;
import com.example.teste.databinding.FragmentGalleryBinding;
import com.example.teste.ui.Dialog.CustomDialogFragment;
import com.example.teste.ui.ScannerFragment.SlideshowFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;


public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private List<Nota> notaList;
    private NotaAdapter notaAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button btnScan = root.findViewById(R.id.btn_scan);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);

        notaList = new ArrayList<>();
        notaAdapter = new NotaAdapter(notaList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(notaAdapter);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(GalleryFragment.this).initiateScan();
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String barcodeResult = result.getContents();
                openCustomDialog(barcodeResult);
                Log.d("Passando codigo que foi lido", "Scanned Code: " + barcodeResult);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void openCustomDialog(String barcodeResult) {
        CustomDialogFragment dialogFragment = CustomDialogFragment.newInstance(barcodeResult);
        dialogFragment.setDialogResult(new CustomDialogFragment.OnDialogResult() {
            @Override
            public void finish(String barcode, String weight, String value, String chaveContingencia) {
                Nota nota = new Nota(barcode, weight, value, chaveContingencia);
                notaList.add(nota);
                notaAdapter.notifyDataSetChanged();
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