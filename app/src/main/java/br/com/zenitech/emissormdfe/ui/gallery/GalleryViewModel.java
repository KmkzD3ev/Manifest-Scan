
package br.com.zenitech.emissormdfe.ui.gallery;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import br.com.zenitech.emissormdfe.Validation.Model.Nota;

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
    public void removeNota(Nota nota) {
        List<Nota> currentNotas = notasLiveData.getValue();
        if (currentNotas != null) {
            currentNotas.remove(nota);
            notasLiveData.setValue(currentNotas);
            Log.d("GalleryViewModel", "Nota removida: " + nota.getChave());
        }
    }

}
