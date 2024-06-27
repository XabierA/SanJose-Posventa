package com.example.SanJose.Models;

public class Localizacion {
    public String audioLocalizacion;

    public String plano;
    //public String plano;

    public Localizacion(String audioLocalizacion, String plano){
        this.audioLocalizacion = audioLocalizacion;
        this.plano = plano;
        //this.plano = plano;
    }

    public String getAudioLocalizacion() {
        return audioLocalizacion;
    }

    public void setAudioLocalizacion(String audioLocalizacion) {
        this.audioLocalizacion = audioLocalizacion;
    }

    /*public String getPlano() {
        return plano;
    }*/

    /*public void setPlano(String plano) {
        this.plano = plano;
    }*/
}
