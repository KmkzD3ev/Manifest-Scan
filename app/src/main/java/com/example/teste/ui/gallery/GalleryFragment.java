package com.example.teste.ui.gallery;

import android.content.Context;
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
import com.example.teste.ApiAuth.ApiManager;
import com.example.teste.Bd.DatabaseHelper;
import com.example.teste.R;
import com.example.teste.Validation.Model.Nota;
import com.example.teste.Validation.Model.NotaDataModel;
import com.example.teste.Validation.Model.UserDataTransferModel;
import com.example.teste.databinding.FragmentGalleryBinding;
import com.example.teste.ui.BarcodeViewModel.BarcodeViewModel;
import com.example.teste.ui.Dialog.CustomDialogFragment;
import com.example.teste.ui.LoadingActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
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

        // FloatingActionButton para ação adicional
        FloatingActionButton fabAdditionalAction = root.findViewById(R.id.fab_additional_action);
        fabAdditionalAction.setOnClickListener(v -> {
            UserDataTransferModel usuarioData = getUserDataTransferModel(getContext());

            if (usuarioData != null) {
                // Converte o objeto UserDataTransferModel para JSON (se necessário)
                Gson gson = new Gson();
                String userDataJson = gson.toJson(usuarioData);

                // Logar os dados para depuração
                Log.d("UsuarioData", "ID: " + usuarioData.getId());
                Log.d("UsuarioData", "Telefone: " + usuarioData.getTelefone());
                Log.d("UsuarioData", "Manifesto: " + (usuarioData.getManifestoDataModel() != null ? usuarioData.getManifestoDataModel().getManifesto() : "N/A"));

                // Logar detalhes das notas
                if (usuarioData.getNotas() != null && !usuarioData.getNotas().isEmpty()) {
                    for (NotaDataModel nota : usuarioData.getNotas()) {
                        Log.d("NotaData", "Chave: " + nota.getChave() + ", Chave_Contingência: " + nota.getChave_Contingencia() +
                                ", Peso: " + nota.getPeso() + ", Valor: " + nota.getValor());
                    }
                } else {
                    Log.d("NotaData", "Nenhuma nota disponível.");
                }

                // Chamada à API
                ApiManager.testApiCall(getContext(), new ApiManager.ApiCallback() {
                    @Override
                    public void onResponse(boolean authorized, String message) {
                        if (authorized) {
                            Log.d("GalleryFragment", "Dados enviados com sucesso!");
                            Snackbar.make(binding.getRoot(), "Dados enviados com sucesso!", Snackbar.LENGTH_LONG).show();
                            // Iniciar a LoadingActivity para visualização de progresso
                            Intent intent = new Intent(getActivity(), LoadingActivity.class);
                            startActivity(intent);
                        } else {
                            Log.d("GalleryFragment", "Falha na autorização: " + message);
                            Snackbar.make(binding.getRoot(), "Falha na autorização: " + message, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Log.d("UsuarioData", "Nenhum usuário está logado.");
                Snackbar.make(binding.getRoot(), "Usuário não está logado.", Snackbar.LENGTH_LONG).show();
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
                    // Verificar se a nota já existe
                    if (notaExists(barcode)) {
                        Snackbar.make(binding.getRoot(), "Nota já existe!", Snackbar.LENGTH_LONG).show();
                    } else {
                        // Criar uma nova Nota e adicionar ao ViewModel
                        Nota nota = new Nota(
                                barcode,
                                weight,
                                value,
                                chaveContingencia
                        );
                        galleryViewModel.addNota(nota);
                        databaseHelper.insertNota(nota, userId); // Passar o userId aqui
                        updateButtonVisibility();

                        Snackbar.make(binding.getRoot(), "Nota salva com sucesso!", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(binding.getRoot(), "Usuário não está logado. Nota não foi salva.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        dialogFragment.show(getChildFragmentManager(), "custom_dialog");
    }

    private boolean notaExists(String barcode) {
        // Verificar no ViewModel
        for (Nota nota : galleryViewModel.getNotas().getValue()) {
            if (nota.getChave().equals(barcode)) {
                return true;
            }
        }
        // Verificar no banco de dados
        int userId = getLoggedInUserId();
        if (userId != -1) {
            List<NotaDataModel> notas = databaseHelper.getAllNotas(userId);
            for (NotaDataModel nota : notas) {
                if (nota.getChave().equals(barcode)) {
                    return true;
                }
            }
        }
        return false;
    }

    private UserDataTransferModel getUserDataTransferModel(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        return dbHelper.getUserDataTransferModel();
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