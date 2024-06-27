package com.example.SanJose.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.SanJose.Models.Incidencia;

import java.io.File;
import java.util.List;

public class IncidenciaViewModel extends ViewModel {

    // Create a LiveData with a String
    private MutableLiveData<Incidencia> incidencia;

    private MutableLiveData<Boolean> editando;
    private MutableLiveData<Boolean> revisando;

    private MutableLiveData<Boolean> enviada;

    private MutableLiveData<List<File>> audios, fotos, videos;

    public MutableLiveData<Incidencia> getCurrentIncidencia() {
        if (incidencia == null) {
            incidencia = new MutableLiveData<Incidencia>();
        }
        return incidencia;
    }

    public MutableLiveData<Boolean> getEditando(){
        if(editando == null){
            editando = new MutableLiveData<Boolean>();
        }
        return editando;
    }

    public MutableLiveData<Boolean> getRevisando(){
        if(revisando == null){
            revisando = new MutableLiveData<Boolean>();
        }
        return revisando;
    }

    public MutableLiveData<Boolean> getEnviada() {
        if(enviada == null){
            enviada = new MutableLiveData<Boolean>();
        }
        return enviada;
    }

    public MutableLiveData<List<File>> getAudios(){
        if (audios == null){
            audios = new MutableLiveData<List<File>>();
        }
        return audios;
    }

    public MutableLiveData<List<File>> getFotos(){
        if (fotos == null){
            fotos = new MutableLiveData<List<File>>();
        }
        return fotos;
    }

    public MutableLiveData<List<File>> getVideos(){
        if (videos == null){
            videos = new MutableLiveData<List<File>>();
        }
        return videos;
    }

// Rest of the ViewModel...
}
