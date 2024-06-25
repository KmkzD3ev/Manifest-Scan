package com.example.teste.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.teste.Validation.Model.Nota;

import java.util.ArrayList;
import java.util.List;

public class GalleryViewModel extends ViewModel {

    private final MutableLiveData<List<Nota>> notasLiveData;

    public GalleryViewModel() {
        notasLiveData = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<Nota>> getNotas() {
        return notasLiveData;
    }

    public void addNota(Nota nota) {
        List<Nota> currentNotas = notasLiveData.getValue();
        currentNotas.add(nota);
        notasLiveData.setValue(currentNotas);
    }
}
