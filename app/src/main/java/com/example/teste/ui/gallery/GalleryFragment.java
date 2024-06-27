package com.example.teste.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teste.Adapter.NotaAdapter;
import com.example.teste.Bd.DatabaseHelper;
import com.example.teste.R;
import com.example.teste.Validation.Model.ManifestoDataModel;
import com.example.teste.Validation.Model.Nota;
import com.example.teste.Validation.Model.NotaDataModel;
import com.example.teste.Validation.Model.UserDataModel;
import com.example.teste.Validation.Model.UserDataTransferModel;
import com.example.teste.databinding.FragmentGalleryBinding;
import com.example.teste.ui.BarcodeViewModel.BarcodeViewModel;
import com.example.teste.ui.Dialog.CustomDialogFragment;
import com.example.teste.ui.LoadingActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
            Intent intent = new Intent(getActivity(), LoadingActivity.class);
            startActivity(intent);
            UserDataTransferModel usuarioData = getUserDataTransferModel(getContext());

            if (usuarioData != null) {
                Log.d("UsuarioData", "id: " + usuarioData.getId());
                Log.d("UsuarioData", "telefone: " + usuarioData.getTelefone());
                Log.d("UsuarioData", "manifesto: " + usuarioData.getManifestoDataModel().getManifesto());

                for (Nota nota : usuarioData.getNotas()) {
                    Log.d("NotaData", "chave: " + nota.getChave());
                    Log.d("NotaData", "chave_contingencia: " + nota.getChave_Contingencia());
                    Log.d("NotaData", "peso: " + nota.getPeso());
                    Log.d("NotaData", "valor: " + nota.getValor());
                }

                // Para exibir o JSON formatado
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String usuarioDataJson = gson.toJson(usuarioData);
                Log.d("UsuarioDataJSON", usuarioDataJson);
                System.out.println(usuarioDataJson);
            } else {
                Log.d("UsuarioData", "Nenhum usuário está logado.");
            }
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
                int userId = getLoggedInUserId();
                Log.d("GalleryFragment", "ID do usuário logado: " + userId);

                Log.d("GalleryFragment", "Dados recebidos do dialog: Barcode: " + barcode +
                        ", Weight: " + weight + ", Value: " + value + ", Chave Contingência: " + chaveContingencia +
                        ", ID do usuário: " + userId);

                if (userId != -1) {
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
                    databaseHelper.insertNota(nota, userId); // Passar o userId aqui
                    updateButtonVisibility();

                    Snackbar.make(binding.getRoot(), "Nota salva com sucesso!", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(binding.getRoot(), "Usuário não está logado. Nota não foi salva.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        dialogFragment.show(getChildFragmentManager(), "custom_dialog");
    }

    private UserDataTransferModel getUserDataTransferModel(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        int id = dbHelper.getLoggedInUserId();

        if (id == -1) {
            return null; // Nenhum usuário está logado
        }

        UserDataModel usuarioDataModel = dbHelper.getLoggedInUserDetails();
        List<NotaDataModel> notaDataModels = dbHelper.getAllNotas(id);
        List<Nota> notas = new ArrayList<>();

        // Converter NotaDataModel para Nota
        for (NotaDataModel notaDataModel : notaDataModels) {
            Nota nota = new Nota(
                    notaDataModel.getChave(),
                    notaDataModel.getPeso(),
                    notaDataModel.getValor(),
                    notaDataModel.getChave_Contingencia()
            );
            notas.add(nota);
        }

        ManifestoDataModel manifestoData = new ManifestoDataModel(dbHelper.getLastManifestoBarcode());

        return new UserDataTransferModel(usuarioDataModel.getId(), usuarioDataModel.getTelefone(), usuarioDataModel.getCodigoValidacao(), manifestoData, notas);
    }


    private int getLoggedInUserId() {
        return databaseHelper.getLoggedInUserId();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
