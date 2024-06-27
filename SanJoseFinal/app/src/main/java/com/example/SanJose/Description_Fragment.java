package com.example.SanJose;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

import com.example.SanJose.Models.Incidencia;
import com.example.SanJose.ViewModels.IncidenciaViewModel;
import com.example.SanJose.databinding.FragmentDescriptionBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Description_Fragment extends Fragment {
    private Context context;

    private IncidenciaViewModel viewModel;

    private Boolean revisandoInc, editandoInc;

    private Incidencia incidenciaActual;

    private Button anteriorBtn, siguienteBtn;

    private EditText etDescripcion, etComplemento1, etComplemento2;

    private String especialidad, disciplina, descripcion, complemento1, complemento2;

    private List<String> disciplinas = new ArrayList<String>();

    private ImageView remoteEyeBtn, zoomBtn;

    public Description_Fragment() {
        // Required empty public constructor
    }

    public static Description_Fragment newInstance() {
        Description_Fragment fragment = new Description_Fragment();
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
        viewModel = new ViewModelProvider(getActivity()).get(IncidenciaViewModel.class);
        final Observer<Boolean> editandoObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean editando) {
                editandoInc = editando;
            }
        };
        viewModel.getEditando().observe(getActivity(), editandoObserver);
        final Observer<Boolean> revisandoObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean revisando) {
                revisandoInc = revisando;
            }
        };
        viewModel.getRevisando().observe(getActivity(), revisandoObserver);
        final Observer<Incidencia> incidenciaObserver = new Observer<Incidencia>() {
            @Override
            public void onChanged(Incidencia incidencia) {
                incidenciaActual = incidencia;
            }
        };
        viewModel.getCurrentIncidencia().observe(getActivity(), incidenciaObserver);
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

        if (editandoInc){
            etDescripcion.setText(incidenciaActual.descripcion);
            descripcion = incidenciaActual.descripcion;
            etComplemento1.setText(incidenciaActual.complemento1);
            complemento1 = incidenciaActual.complemento1;
            etComplemento2.setText(incidenciaActual.complemento2);
            complemento2 = incidenciaActual.complemento2;
        }

        if (revisandoInc){
            etDescripcion.setText(incidenciaActual.descripcion);
            descripcion = incidenciaActual.descripcion;
            etComplemento1.setText(incidenciaActual.complemento1);
            complemento1 = incidenciaActual.complemento1;
            etComplemento2.setText(incidenciaActual.complemento2);
            complemento2 = incidenciaActual.complemento2;
        }

        if (getArguments().getString("especialidad") != "1"){
            especialidad = getArguments().getString("especialidad");
        }else {
            especialidad = "1";
        }
        if (getArguments().getString("disciplina") != "1"){
            disciplina = getArguments().getString("disciplina");
        }else {
            disciplina = "";
        }

        if (getArguments().getSerializable("disciplinas") != null){
            disciplinas = (List<String>) getArguments().getSerializable("disciplinas");
        }


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
                Data_Fragment f = new Data_Fragment().newInstance();
                Bundle args = new Bundle();
                args.putString("descripcion", descripcion);
                args.putString("complemento1", complemento1);
                args.putString("complemento2", complemento2);
                args.putString("especialidad", especialidad);
                //args.putString("disciplina", disciplina);
                args.putSerializable("disciplinas", (Serializable) disciplinas);
                args.putSerializable("usuario", getArguments().getSerializable("usuario"));
                args.putSerializable("proyecto", getArguments().getSerializable("proyecto"));
                f.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container, f).addToBackStack("descripcion").commit();
            }
        });
    }
}