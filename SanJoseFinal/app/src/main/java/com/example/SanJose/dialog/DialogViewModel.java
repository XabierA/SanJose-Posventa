package com.example.SanJose.dialog;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DialogViewModel extends ViewModel {

    private MutableLiveData<String> imagen;

    public DialogViewModel() {
        this.imagen = new MutableLiveData<>();
        imagen.postValue("a");
    }

    public void setImagen(String imagen){
        this.imagen = new MutableLiveData<>();
        this.imagen.postValue(imagen);
    }

    public MutableLiveData<String> getImagen() {
        return imagen;
    }
}
