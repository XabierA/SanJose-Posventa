package com.example.SanJose;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.SanJose.FragmentCallBack.CallBackIncidencia;
import com.example.SanJose.FragmentCallBack.FragmentCallBack;
import com.example.SanJose.Models.Incidencia;
import com.example.SanJose.Models.User;
import com.example.SanJose.ViewModels.IncidenciaViewModel;
import com.example.SanJose.ViewModels.InspeccionViewModel;

import java.io.File;
import java.io.Serializable;

public class MainMenuFragment extends Fragment implements CallBackIncidencia {

    private Context context;

    private IncidenciaViewModel viewModel;
    private InspeccionViewModel viewModel2;

    private Incidencia incidenciaActual;

    private Boolean editandoInc, revisandoInc, incEnviada;
    private Boolean editandoIns, revisandoIns, insEnviada;

    private ImageButton add_IncBtn, add_InsBtn;

    private ImageView zoomBtn, remoteEyeBtn;

    private TextView cerrarsesion;

    private Button incidenciaBtn;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    public static MainMenuFragment newInstance() {
        MainMenuFragment fragment = new MainMenuFragment();
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
        final Observer<Incidencia> incidenciaObserver = new Observer<Incidencia>() {
            @Override
            public void onChanged(Incidencia incidencia) {
                incidenciaActual = incidencia;
            }
        };
        viewModel.getCurrentIncidencia().observe(getActivity(), incidenciaObserver);
        final Observer<Boolean> editandoObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean editando) {
                editandoInc = editando;
            }
        };
        viewModel.getEditando().observe(getActivity(), editandoObserver);
        viewModel.getEditando().setValue(false);
        final Observer<Boolean> revisandoObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean revisando) {
                revisandoInc = revisando;
            }
        };
        viewModel.getRevisando().observe(getActivity(), revisandoObserver);
        viewModel.getRevisando().setValue(false);
        final Observer<Boolean> enviadaObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean enviada) {
                incEnviada = enviada;
            }
        };
        viewModel.getEnviada().observe(getActivity(), enviadaObserver);
        viewModel.getEnviada().setValue(false);

        viewModel2 = new ViewModelProvider(getActivity()).get(InspeccionViewModel.class);
        final Observer<Boolean> revisandoInsObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean revisando) {
                revisandoIns = revisando;
            }
        };
        viewModel2.getRevisando().observe(getActivity(), revisandoInsObserver);
        viewModel2.getRevisando().setValue(false);

        final Observer<Boolean> editandoInsObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean revisando) {
                editandoIns = revisando;
            }
        };
        viewModel2.getEditando().observe(getActivity(), editandoInsObserver);
        viewModel2.getEditando().setValue(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        add_IncBtn = getView().findViewById(R.id.btn_add_inc);
        add_InsBtn = getView().findViewById(R.id.btn_add_ins);
        zoomBtn = getView().findViewById(R.id.zoom_menu);
        remoteEyeBtn = getView().findViewById(R.id.remoteeye_menu);
        cerrarsesion = getView().findViewById(R.id.cerrarSesion);
        incidenciaBtn = getView().findViewById(R.id.btnIncidencias);

        Log.v("usu", ((User)getArguments().getSerializable("usuario")).username);

        add_IncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel.getRevisando().getValue()){
                    viewModel.getRevisando().setValue(false);
                }
                Esp_Disc_Fragment f = new Esp_Disc_Fragment();
                Bundle args = new Bundle();
                args.putSerializable("usuario", getArguments().getSerializable("usuario"));
                args.putSerializable("proyecto", getArguments().getSerializable("proyecto"));
                f.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container,  f).addToBackStack("main").commit();
            }
        });

        add_InsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel2.getRevisando().getValue()){
                    viewModel2.getRevisando().setValue(false);
                }
                Esp_Disc_Inspection_Fragment f = new Esp_Disc_Inspection_Fragment();
                Bundle args = new Bundle();
                args.putSerializable("usuario", getArguments().getSerializable("usuario"));
                args.putSerializable("proyecto", getArguments().getSerializable("proyecto"));
                f.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container,  f).addToBackStack("main").commit();
            }
        });

        zoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("us.zoom.videomeetings");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                }
            }
        });

        remoteEyeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.wideum.remoteeye");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                }
            }
        });

        cerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        incidenciaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incidenciaActual != null){
                    if (viewModel.getEnviada().getValue()){
                        Toast.makeText(context, "La incidencia ya ha sido enviada", Toast.LENGTH_LONG).show();
                    }else {
                        viewModel.getEditando().setValue(true);
                        Esp_Disc_Fragment f = new Esp_Disc_Fragment();
                        Bundle args = new Bundle();
                        args.putSerializable("usuario", getArguments().getSerializable("usuario"));
                        args.putSerializable("proyecto", getArguments().getSerializable("proyecto"));
                        f.setArguments(args);
                        getParentFragmentManager().beginTransaction().replace(R.id.nav_container,  f).addToBackStack("main").commit();
                    }
                }else {
                    Toast.makeText(context, "No hay incidencias anteriores cargadas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onIncidenciaSent(Incidencia incidencia) {
        incidenciaActual = incidencia;
    }
}