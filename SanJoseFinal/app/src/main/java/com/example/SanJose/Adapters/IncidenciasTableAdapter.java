package com.example.SanJose.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SanJose.MainActivity;
import com.example.SanJose.Models.Incidencia;
import com.example.SanJose.Models.User;
import com.example.SanJose.R;
import com.example.SanJose.RevisionFragment;
import com.example.SanJose.Revision_Inc_Ins_Fragment;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IncidenciasTableAdapter extends RecyclerView.Adapter<IncidenciasTableAdapter.MyViewHolder>{
    private List<Incidencia> incidencias = new ArrayList<>();
    private User usuario;
    private Context context;

    public IncidenciasTableAdapter(List<Incidencia> incidencias, User usuario) {
        this.incidencias = incidencias;
        this.usuario = usuario;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView codigo, estado, autor, fecha, proyecto, disciplina, especialidad;

        public MyViewHolder(final View view) {
            super(view);
            codigo = view.findViewById(R.id.inc_tableCodigo);
            estado = view.findViewById(R.id.inc_tableEstado);
            autor = view.findViewById(R.id.inc_tableAutor);
            fecha = view.findViewById(R.id.inc_tableFecha);
            proyecto = view.findViewById(R.id.inc_tableProyecto);
            disciplina = view.findViewById(R.id.inc_tableDisciplina);
            especialidad = view.findViewById(R.id.inc_tableEspecialidad);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.incidencia_item, parent, false);
        return new MyViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String textDisc = "";

        for (int i2 = 0; i2 < incidencias.get(position).disciplinas.size(); i2++){
            if (i2 == incidencias.get(position).disciplinas.size()-1){
                textDisc = textDisc+incidencias.get(position).disciplinas.get(i2);
            }else {
                textDisc = textDisc+incidencias.get(position).disciplinas.get(i2)+", ";
            }
        }

        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        DateFormat inputFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy", Locale.US);

        String inputText = incidencias.get(position).fechaCreacion+"";
        Date date = null;
        try {
            date = inputFormat.parse(inputText);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String outputText = outputFormat.format(date);

        holder.codigo.setText(" "+incidencias.get(position).codigo+"   "+incidencias.get(position).proyectoCod+"   "+incidencias.get(position).autorNombre+"   "+outputText+"   "+incidencias.get(position).estado);
        holder.codigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Revision_Inc_Ins_Fragment f = new Revision_Inc_Ins_Fragment();
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putSerializable("incidencia", incidencias.get(position));
                bundle.putSerializable("usuario", usuario);
                f.setArguments(bundle);
                manager.beginTransaction().replace(R.id.nav_container, f).addToBackStack("list_inc").commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return incidencias.size();
    }
}
