package com.example.SanJose;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.SanJose.Models.Incidencia;
import com.example.SanJose.Models.Inspeccion;
import com.example.SanJose.Models.User;
import com.example.SanJose.ViewModels.IncidenciaViewModel;
import com.example.SanJose.ViewModels.InspeccionViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Description_Ins_Fragment extends Fragment {
    private Context context;

    private InspeccionViewModel viewModel;

    private Boolean revisandoInc;

    private Incidencia incidencia;

    private User user;

    private Inspeccion inspeccionActual;

    private String descripcion, complemento1, complemento2;

    private Button anteriorBtn, siguienteBtn;

    private EditText etDescripcion, etComplemento1, etComplemento2;

    private ImageView remoteEyeBtn, zoomBtn;

    public Description_Ins_Fragment() {
        // Required empty public constructor
    }

    public static Description_Ins_Fragment newInstance() {
        Description_Ins_Fragment fragment = new Description_Ins_Fragment();
        /*Bundle arguments = new Bundle();
        arguments.putString("test", "test");
        fragment.setArguments(arguments);*/
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        this.context = context;
        //control = (IControlFragmentos) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(InspeccionViewModel.class);
        final Observer<Boolean> revisandoObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean revisando) {
                revisandoInc = revisando;
            }
        };
        viewModel.getRevisando().observe(getActivity(), revisandoObserver);
        final Observer<Inspeccion> inspeccionObserver = new Observer<Inspeccion>() {
            @Override
            public void onChanged(Inspeccion inspeccion) {
                inspeccionActual = inspeccion;
            }
        };
        viewModel.getCurrentInspeccion().observe(getActivity(), inspeccionObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_description_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        remoteEyeBtn = getView().findViewById(R.id.remoteeye_desc);
        zoomBtn = getView().findViewById(R.id.zoom_desc);
        etDescripcion = getView().findViewById(R.id.etDescripcion);
        etComplemento1 = getView().findViewById(R.id.etC1);
        etComplemento2 = getView().findViewById(R.id.etC2);
        anteriorBtn = getView().findViewById(R.id.btnAnterior_Docs);
        siguienteBtn = getView().findViewById(R.id.btnSiguiente_Docs);


        if (revisandoInc){
            etDescripcion.setText(inspeccionActual.descripcion);
            descripcion = inspeccionActual.descripcion;
            etComplemento1.setText(inspeccionActual.complemento1);
            complemento1 = inspeccionActual.complemento1;
            etComplemento2.setText(inspeccionActual.complemento2);
            complemento2 = inspeccionActual.complemento2;
        }

        user = (User) getArguments().getSerializable("usu");
        incidencia = (Incidencia) getArguments().getSerializable("incidencia");


        zoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("us.zoom.videomeetings");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        });
        remoteEyeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.wideum.remoteeye");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        });

        anteriorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStackImmediate();
            }
        });

        siguienteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etDescripcion != null){
                    descripcion = etDescripcion.getText().toString();
                }else {
                    descripcion = "";
                }if (etComplemento1 != null){
                    complemento1 = etComplemento1.getText().toString();
                }else {
                    complemento1 = "";
                }if (etComplemento2 != null){
                    complemento2 = etComplemento2.getText().toString();
                }else {
                    complemento2 = "";
                }
                Data_Ins_Fragment f = new Data_Ins_Fragment().newInstance();
                Bundle args = new Bundle();
                args.putString("descripcion", descripcion);
                args.putString("complemento1", complemento1);
                args.putString("complemento2", complemento2);
                args.putSerializable("usuario", getArguments().getSerializable("usu"));
                args.putSerializable("incidencia", incidencia);
                f.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container, f).addToBackStack("descripcion_ins").commit();
            }
        });
    }
}