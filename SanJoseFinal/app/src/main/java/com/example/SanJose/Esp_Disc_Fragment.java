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
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.SanJose.Models.Incidencia;
import com.example.SanJose.ViewModels.IncidenciaViewModel;
import com.example.SanJose.databinding.FragmentEspDiscBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Esp_Disc_Fragment extends Fragment {
    private Context context;

    private IncidenciaViewModel viewModel;

    private Incidencia incidenciaActual;

    private Boolean revisandoInc, editandoInc;

    private Button anteriorBtn, siguienteBtn;

    private ImageView zoomBtn, remoteEyeBtn;

    private CheckBox dc1, dc2, dc3, dc4, electricidad, fontaneria, estructuras, otras;

    private String discDc1, discDc2, discDc3, discDc4, discElectricidad, discFontaneria, discEstructuras, discOtras;

    private List<String> disciplinas;

    public Esp_Disc_Fragment() {
        // Required empty public constructor
    }

    public static Esp_Disc_Fragment newInstance() {
        Esp_Disc_Fragment fragment = new Esp_Disc_Fragment();
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

        return inflater.inflate(R.layout.fragment_esp__disc_, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        remoteEyeBtn = getView().findViewById(R.id.remoteeye_esp_dsc);
        zoomBtn = getView().findViewById(R.id.zoom_esp_dsc);
        electricidad = getView().findViewById(R.id.chElectricidad);
        fontaneria = getView().findViewById(R.id.chFontaneria);
        estructuras = getView().findViewById(R.id.chEstructura);
        otras = getView().findViewById(R.id.chOtras);
        dc1 = getView().findViewById(R.id.chDc1);
        dc2 = getView().findViewById(R.id.chDc2);
        dc3 = getView().findViewById(R.id.chDc3);
        dc4 = getView().findViewById(R.id.chDc4);
        siguienteBtn = getView().findViewById(R.id.btnSiguiente_Esp_Dsc);
        anteriorBtn = getView().findViewById(R.id.btnAnterior_Esp_Dsc);

        if(editandoInc){
            for (String disciplinaX:incidenciaActual.disciplinas) {
                switch (disciplinaX) {
                    case "Electricidad":
                        discElectricidad = disciplinaX;
                        electricidad.setChecked(true);
                        break;
                    case "Fontaneria":
                        discFontaneria = disciplinaX;
                        fontaneria.setChecked(true);
                        break;
                    case "Estructuras":
                        discEstructuras = disciplinaX;
                        estructuras.setChecked(true);
                        break;
                    case "Otras":
                        discOtras = disciplinaX;
                        otras.setChecked(true);
                        break;
                    case "Disciplina 1":
                        discDc1 = disciplinaX;
                        dc1.setChecked(true);
                        break;
                    case "Disciplina 2":
                        discDc2 = disciplinaX;
                        dc2.setChecked(true);
                        break;
                    case "Disciplina 3":
                        discDc3 = disciplinaX;
                        dc3.setChecked(true);
                        break;
                    case "Disciplina 4":
                        discDc4 = disciplinaX;
                        dc4.setChecked(true);
                        break;
                }
            }
        }

        if(revisandoInc){
            for (String disciplinaX:incidenciaActual.disciplinas) {
                switch (disciplinaX) {
                    case "Electricidad":
                        discElectricidad = disciplinaX;
                        electricidad.setChecked(true);
                        break;
                    case "Fontaneria":
                        discFontaneria = disciplinaX;
                        fontaneria.setChecked(true);
                        break;
                    case "Estructuras":
                        discEstructuras = disciplinaX;
                        estructuras.setChecked(true);
                        break;
                    case "Otras":
                        discOtras = disciplinaX;
                        otras.setChecked(true);
                        break;
                    case "Disciplina 1":
                        discDc1 = disciplinaX;
                        dc1.setChecked(true);
                        break;
                    case "Disciplina 2":
                        discDc2 = disciplinaX;
                        dc2.setChecked(true);
                        break;
                    case "Disciplina 3":
                        discDc3 = disciplinaX;
                        dc3.setChecked(true);
                        break;
                    case "Disciplina 4":
                        discDc4 = disciplinaX;
                        dc4.setChecked(true);
                        break;
                }
            }
        }


        electricidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(electricidad.isChecked()){
                    discElectricidad = "Electricidad";
                }else {
                    discElectricidad = null;
                }
            }
        });
        fontaneria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fontaneria.isChecked()){
                    discFontaneria = "Fontaneria";
                }else {
                    discFontaneria = null;
                }
            }
        });
        estructuras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(estructuras.isChecked()){
                    discEstructuras = "Estructuras";
                }else {
                    discEstructuras = null;
                }
            }
        });
        otras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otras.isChecked()){
                    discOtras = "Otras";
                }else {
                    discOtras = null;
                }
            }
        });

        dc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dc1.isChecked()){
                    discDc1 = "Disciplina 1";
                }else {
                    discDc1 = null;
                }
            }
        });
        dc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dc2.isChecked()){
                    discDc2 = "Disciplina 2";
                }else {
                    discDc2 = null;
                }
            }
        });
        dc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dc3.isChecked()){
                    discDc3 = "Disciplina 3";
                }else {
                    discDc3 = null;
                }
            }
        });
        dc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dc4.isChecked()){
                    discDc4 = "Disciplina 4";
                }else {
                    discDc4 = null;
                }
            }
        });

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
                viewModel.getEditando().setValue(false);
                viewModel.getRevisando().setValue(false);
                getParentFragmentManager().popBackStackImmediate();
            }
        });
        siguienteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disciplinas = new ArrayList<String>();

                if (discElectricidad != null){
                    disciplinas.add(discElectricidad);
                } if(discEstructuras != null){
                    disciplinas.add(discEstructuras);
                } if (discFontaneria != null) {
                    disciplinas.add(discFontaneria);
                } if (discOtras != null) {
                    disciplinas.add(discOtras);
                } if(discDc1 != null){
                    disciplinas.add(discDc1);
                } if (discDc2 != null) {
                    disciplinas.add(discDc2);
                } if (discDc3 != null) {
                    disciplinas.add(discDc3);
                } if (discDc4 != null) {
                    disciplinas.add(discDc4);
                }

                Description_Fragment f = new Description_Fragment();
                Bundle args = new Bundle();
                if(disciplinas != null){
                    args.putSerializable("disciplinas", (Serializable) disciplinas);
                }
                args.putSerializable("usuario", getArguments().getSerializable("usuario"));
                args.putSerializable("proyecto", getArguments().getSerializable("proyecto"));
                f.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container, f).addToBackStack("disciplina").commit();
            }
        });
    }
}