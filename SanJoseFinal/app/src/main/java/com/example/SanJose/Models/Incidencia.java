package com.example.SanJose.Models;

import androidx.annotation.Nullable;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Incidencia implements Serializable {

    public String _id;

    public String codigo;

    public String autor;

    public String autorNombre;

    public Date fechaCreacion;

    public String proyecto;

    public String disciplina;

    public String complemento1;

    public String complemento2;

    public List<String> disciplinas = new ArrayList<String>();

    public String descripcion;

    public String estado;

    public String proyectoCod;

    public Documentos documentos;

    public Localizacion localizacion;

    public Boolean numExtraAudios, numExtraFotos, numExtravideos;

    public Incidencia(){

    };
    public Incidencia(String codigo, String autor, String autorNombre, Date fechaCreacion, String proyecto, String disciplina, String descripcion, String complemento1, String complemento2, String estado, @Nullable String proyectoCod){
        this.codigo = codigo;
        this.autor = autor;
        this.autorNombre = autorNombre;
        this.fechaCreacion = fechaCreacion;
        this.proyecto = proyecto;
        this.disciplina = disciplina;
        this.descripcion = descripcion;
        this.complemento1 = complemento1;
        this.complemento2 = complemento2;
        this.estado = estado;
        this.proyectoCod = proyectoCod;
        //AÑADIR DE NUEVO DOCUMENTOS CUANDO SE ARREGLE LO DE PODER SUBIR ARCHIVOS
        //this.documentos = documentos;
    }

    public String getObjectId(){
        return _id;
    }

    public void setObjectId(String objectId) {
        this._id = objectId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getAutorNombre() {
        return autorNombre;
    }

    public void setAutorNombre(String autorNombre) {
        this.autorNombre = autorNombre;
    }

    public Date getFecha() {
        return fechaCreacion;
    }

    public void setFecha(Date fecha) {
        this.fechaCreacion = fecha;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public List<String> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<String> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComplemento1() {
        return complemento1;
    }

    public void setComplemento1(String complemento1) {
        this.complemento1 = complemento1;
    }

    public String getComplemento2() {
        return complemento2;
    }

    public void setComplemento2(String complemento2) {
        this.complemento2 = complemento2;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getProyectoCod() {
        return proyectoCod;
    }

    public void setProyectoCod(String proyectoCod) {
        this.proyectoCod = proyectoCod;
    }

    public Documentos getDocumentos() {
        return documentos;
    }

    public void setDocumentos(Documentos documentos) {
        this.documentos = documentos;
    }

    public Localizacion getLocalizacion(){
        return localizacion;
    }

    public void setLocalizacion(Localizacion localizacion){
        this.localizacion = localizacion;
    }

    public Boolean getNumExtraAudios() {
        return numExtraAudios;
    }

    public void setNumExtraAudios(Boolean numExtraAudios) {
        this.numExtraAudios = numExtraAudios;
    }

    public Boolean getNumExtraFotos() {
        return numExtraFotos;
    }

    public void setNumExtraFotos(Boolean numExtraFotos) {
        this.numExtraFotos = numExtraFotos;
    }

    public Boolean getNumExtravideos() {
        return numExtravideos;
    }

    public void setNumExtravideos(Boolean numExtravideos) {
        this.numExtravideos = numExtravideos;
    }
}
