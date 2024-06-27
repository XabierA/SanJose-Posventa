package com.example.SanJose.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Conforme implements Serializable {
    List<String> disciplinas = new ArrayList<>();
    String proyecto;
    String conforme;

    public Conforme(){

    };

    public List<String> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<String> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }
}