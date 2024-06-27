package com.example.SanJose.Models;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Inspeccion implements Serializable {

    public String objectId;

    public String codigo;

    public String autor;

    public String autorNombre;

    public Date fechaCreacion;

    public String incidencia;

    public String proyecto;

    public String descripcion;

    public String complemento1;

    public String complemento2;
    public String estado;

    public Documentos documentos;

    public Boolean numExtraAudios, numExtraFotos, numExtravideos;

    public Inspeccion(){

    };
    public Inspeccion(String codigo, String autor, String autorNombre, Date fechaCreacion, String descripcion, String complemento1, String complemento2, String estado, String incidencia, String proyecto){
        this.codigo = codigo;
        this.autor = autor;
        this.autorNombre = autorNombre;
        this.fechaCreacion = fechaCreacion;
        this.descripcion = descripcion;
        this.complemento1 = complemento1;
        this.complemento2 = complemento2;
        this.estado = estado;
        this.incidencia = incidencia;
        this.proyecto = proyecto;
    }

    public String getObjectId(){
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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

    public String getIncidencia() {
        return incidencia;
    }

    public void setIncidencia(String incidencia) {
        this.incidencia = incidencia;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public Documentos getDocumentos() {
        return documentos;
    }

    public void setDocumentos(Documentos documentos) {
        this.documentos = documentos;
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
