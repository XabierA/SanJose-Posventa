package com.example.SanJose.Models;

import java.io.Serializable;

public class Documentos implements Serializable {

    /*public List<File> imagenes;

    public List<File> audios;

    public File video;

    public Documentos(List<File> imagenes, List<File> audios, File video){
        this.imagenes = imagenes;
        this.audios = audios;
        this.video = video;
    }

    public List<File> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<File> imagenes) {
        this.imagenes = imagenes;
    }

    public List<File> getAudios() {
        return audios;
    }

    public void setAudios(List<File> audios) {
        this.audios = audios;
    }

    public File getVideo() {
        return video;
    }

    public void setVideo(File video) {
        this.video = video;
    }*/
    ///////////////////////////////////////////////////
    /*public String foto;
    public String audio;
    public String video;

    public Documentos(String foto, String audio, String video){
        this.foto = foto;
        this.audio = audio;
        this.video = video;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String audio) {
        this.video = video;
    }*/
    /////////////////////////////////////////////////////////
    public String[] audios;
    public String[] fotos;
    public String[] videos;

    public Documentos(String[] audios, String[] fotos, String[] videos){
        this.audios = audios;
        this.fotos = fotos;
        this.videos = videos;
    }

    public String[] getAudios() {
        return audios;
    }

    public void setAudios(String[] audios) {
        this.audios = audios;
    }

    public String[] getFotos() {
        return fotos;
    }

    public void setFotos(String[] fotos) {
        this.fotos = fotos;
    }

    public String[] getVideos() {
        return videos;
    }

    public void setVideos(String[] videos) {
            this.videos = videos;
    }
}
