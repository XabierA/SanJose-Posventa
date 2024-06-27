package com.example.SanJose.Models;

import androidx.annotation.Nullable;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.Date;

public class Proyecto implements Serializable {
    public String _id;
    public String codigo;
    public String nombre;
    public String autor;
    public String autorNombre;
    public String[] documentos;
    public Date fechaCreacion;
    public boolean activo;
    public int __v;
    public int numInc;
    public int numIns;

    public Proyecto(@Nullable String codigo, @Nullable String nombre, @Nullable String autor, @Nullable String autorNombre, @Nullable String[] documentos, @Nullable Date fechaCreacion, @Nullable boolean activo, @Nullable int __v, @Nullable int numInc, @Nullable int numIns){
        this.codigo = codigo;
        this.nombre = nombre;
        this.autor = autor;
        this.autorNombre = autorNombre;
        this.documentos = documentos;
        this.fechaCreacion = fechaCreacion;
        this.activo = activo;
        this.__v = __v;
        this.numInc = numInc;
        this.numIns = numIns;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String[] getDocumentos() {
        return documentos;
    }

    public void setDocumentos(String[] documentos) {
        this.documentos = documentos;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
