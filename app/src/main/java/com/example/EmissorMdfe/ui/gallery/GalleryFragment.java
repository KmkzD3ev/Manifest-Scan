package com.example.EmissorMdfe.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EmissorMdfe.Adapter.NotaAdapter;
import com.example.EmissorMdfe.ApiAuth.ApiManager;
import com.example.EmissorMdfe.Bd.DatabaseHelper;
import com.example.EmissorMdfe.R;
import com.example.EmissorMdfe.Validation.Model.Nota;
import com.example.EmissorMdfe.Validation.Model.NotaDataModel;
import com.example.EmissorMdfe.Validation.Model.UserDataTransferModel;
import com.example.EmissorMdfe.databinding.FragmentGalleryBinding;
import com.example.EmissorMdfe.ui.BarcodeViewModel.BarcodeViewModel;
import com.example.EmissorMdfe.ui.Dialog.CustomDialogFragment;
import com.example.EmissorMdfe.ui.LoadingActivity;
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

        notaAdapter = new NotaAdapter(getContext(), new ArrayList<>(), galleryViewModel);
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
                Log.d("Gallery SEND", "ID: " + usuarioData.getId());
                Log.d("Gallery SEND", "Telefone: " + usuarioData.getTelefone());
                Log.d("Gallery SEND", "Manifesto: " + (usuarioData.getManifestoDataModel() != null ? usuarioData.getManifestoDataModel().getManifesto() : "N/A"));

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
                            Log.d("GalleryFragment", "Dados enviados com sucesso!" + message);
                            Toast.makeText(getContext(), " " + message, Toast.LENGTH_LONG).show();

                            // Atraso para garantir que o Toast seja visível antes de iniciar a LoadingActivity
                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Iniciar a LoadingActivity para visualização de progresso
                                    Intent intent = new Intent(getActivity(), LoadingActivity.class);
                                    startActivity(intent);
                                }
                            }, 3500); // Duração do Toast.LENGTH_LONG é de 3500 ms
                        } else {
                            Log.d("Resposta do Envio Float", "Falha na autorização: " + message);
                            Toast.makeText(getContext(), "ERROR: " + message, Toast.LENGTH_LONG).show();
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

    //ATUALIZA A INTERFACE DE ACORDO COM A QUANTIDADE DE NOTAS
    private void updateButtonVisibility() {
        FloatingActionButton fabAdditionalAction = binding.fabAdditionalAction;
        if (notaAdapter.getItemCount() > 0) {
            fabAdditionalAction.show();
        } else {
            fabAdditionalAction.hide();
        }
    }

    //Processa o resultado do scan realizado pelo btn_scan
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

    //Verifica se a nota já existe no ViewModel ou no banco de dados para evitar duplicicidades
    private boolean notaExists(String barcode) {
        Log.d("notaExists", "Verificando existência da nota com barcode: " + barcode);

        // Verificar no ViewModel
        List<Nota> currentNotas = galleryViewModel.getNotas().getValue();
        if (currentNotas != null) {
            for (Nota nota : currentNotas) {
                if (nota.getChave().equals(barcode)) {
                    Log.d("notaExists", "Nota encontrada no ViewModel com barcode: " + barcode);
                    return true;
                }
            }
        } else {
            Log.d("notaExists", "Nenhuma nota encontrada no ViewModel.");
        }

        // Verificar no banco de dados
        int userId = getLoggedInUserId();
        Log.d("notaExists", "ID do usuário logado: " + userId);
        if (userId != -1) {
            List<NotaDataModel> notas = databaseHelper.getAllNotas(userId);
            for (NotaDataModel nota : notas) {
                if (nota.getChave().equals(barcode)) {
                    Log.d("notaExists", "Nota encontrada no banco de dados com barcode: " + barcode);
                    return true;
                }
            }
            Log.d("notaExists", "Nenhuma nota encontrada no banco de dados para o usuário: " + userId);
        } else {
            Log.d("notaExists", "Nenhum usuário logado.");
        }

        Log.d("notaExists", "Nota com barcode: " + barcode + " não encontrada.");
        return false;
    }


   //obtem o conjunto de dados do usuário logado
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

