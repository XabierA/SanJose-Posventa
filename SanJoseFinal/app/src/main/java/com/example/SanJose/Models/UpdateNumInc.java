package com.example.SanJose.Models;

import java.util.Date;

public class UpdateNumInc {

    private String codigoProyecto;

    private int numInc;

    public UpdateNumInc(){

    };
    public UpdateNumInc(String codigoProyecto, int numInc){
        this.codigoProyecto = codigoProyecto;
        this.numInc = numInc;
    }


    public String getcodigoProyecto() {
        return codigoProyecto;
    }

    public void setcodigoProyecto(String codigoProyecto) {
        this.codigoProyecto = codigoProyecto;
    }

    public int getNumInc() {
        return numInc;
    }

    public void setNumInc(int numInc) {
        this.numInc = numInc;
    }
}
