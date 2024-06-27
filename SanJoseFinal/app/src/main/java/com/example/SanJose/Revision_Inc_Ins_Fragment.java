package com.example.SanJose;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.SanJose.FragmentCallBack.FragmentCallBack;
import com.example.SanJose.Models.Documentos;
import com.example.SanJose.Models.Incidencia;
import com.example.SanJose.Models.Inspeccion;
import com.example.SanJose.Models.Localizacion;
import com.example.SanJose.Models.Proyecto;
import com.example.SanJose.Models.UpdateNumInc;
import com.example.SanJose.Models.User;
import com.example.SanJose.Models.response;
import com.example.SanJose.ViewModels.Image_Gallery_Fragment;
import com.example.SanJose.ViewModels.IncidenciaViewModel;
import com.example.SanJose.ViewModels.InspeccionViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public class Revision_Inc_Ins_Fragment extends Fragment {
    private Context context;

    private InspeccionViewModel viewModel;

    private Incidencia incidencia;

    private User usuario;

    private String disciplina, especialidad, descripcion, complemento1, complemento2;


    private Button audiosBtn, fotosBtn, videosBtn, btnAnterior, btnSiguiente;

    private ImageView remoteEye, zoomBtn;

    private TextView tvDisciplina, tvDescripcion, tvComplemento1, tvComplemento2;


    private List<File> audios = new ArrayList<File>();
    private List<File> fotos = new ArrayList<File>();
    private List<File> videos = new ArrayList<File>();

    public Revision_Inc_Ins_Fragment() {
        // Required empty public constructor
    }

    public static Revision_Inc_Ins_Fragment newInstance() {
        Revision_Inc_Ins_Fragment fragment = new Revision_Inc_Ins_Fragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_revision_inc_ins, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        remoteEye = getView().findViewById(R.id.remoteeye_rev_inc_ins);
        zoomBtn = getView().findViewById(R.id.zoom_rev_inc_ins);
        audiosBtn = getView().findViewById(R.id.btnAudios_rev_inc_ins);
        fotosBtn = getView().findViewById(R.id.btnFotos_rev_inc_ins);
        videosBtn = getView().findViewById(R.id.btnVideos_rev_inc_ins);
        btnAnterior = getView().findViewById(R.id.btnAnterior_rev_inc_ins);
        btnSiguiente = getView().findViewById(R.id.btnSiguiente_rev_inc_ins);
        tvDisciplina = getView().findViewById(R.id.rev_inc_ins_disciplina);
        tvDescripcion = getView().findViewById(R.id.rev_inc_ins_descripcion);
        tvComplemento1 = getView().findViewById(R.id.rev_inc_ins_complemento1);
        tvComplemento2 = getView().findViewById(R.id.rev_inc_ins_complemento2);

        incidencia = (Incidencia) getArguments().getSerializable("incidencia");
        usuario = (User) getArguments().getSerializable("usuario");

        disciplina = getArguments().getString("disciplina");

        String textDisc = "";

        for (int i2 = 0; i2 < incidencia.disciplinas.size(); i2++){
            if (i2 == incidencia.disciplinas.size()-1){
                textDisc = textDisc+incidencia.disciplinas.get(i2);
            }else {
                textDisc = textDisc+incidencia.disciplinas.get(i2)+", ";
            }
        }

        tvDisciplina.setText(textDisc);
        tvDescripcion.setText(incidencia.descripcion);
        tvComplemento1.setText(incidencia.complemento1);
        tvComplemento2.setText(incidencia.complemento2);

        if (incidencia.documentos != null && audios.isEmpty()){
            if(incidencia.documentos.audios != null){
                if (incidencia.documentos.audios.length > 0){
                    DownloadAmazonS3 dAWS1 = new DownloadAmazonS3();
                    dAWS1.setType("audio");
                    dAWS1.setContext(context);
                    dAWS1.setAudioNames(incidencia.documentos.audios);
                    dAWS1.execute(incidencia.documentos.audios);
                    audios = dAWS1.onPostExecute("audios");
                }
            }
        } if (incidencia.documentos != null){
            if(incidencia.documentos.fotos != null && fotos.isEmpty()){
                if (incidencia.documentos.fotos.length > 0){
                    DownloadAmazonS3 dAWS2 = new DownloadAmazonS3();
                    dAWS2.setType("foto");
                    dAWS2.setContext(context);
                    dAWS2.setFotoNames(incidencia.documentos.fotos);
                    dAWS2.execute(incidencia.documentos.fotos);
                    fotos = dAWS2.onPostExecute("fotos");
                }
            }
        } if (incidencia.documentos != null && videos.isEmpty()){
            if(incidencia.documentos.videos != null){
                if (incidencia.documentos.videos.length > 0){
                    DownloadAmazonS3 dAWS3 = new DownloadAmazonS3();
                    dAWS3.setType("video");
                    dAWS3.setContext(context);
                    dAWS3.setVideoNames(incidencia.documentos.videos);
                    dAWS3.execute(incidencia.documentos.videos);
                    videos = dAWS3.onPostExecute("videos");
                }
            }
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
        remoteEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.wideum.remoteeye");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        });

        audiosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putSerializable("audios", (Serializable) audios);
                Audio_Gallery_Fragment audioGallery = Audio_Gallery_Fragment.newInstance();
                audioGallery.setArguments(result);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container, audioGallery).addToBackStack(null).commit();
            }
        });

        fotosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putSerializable("imagenes", (Serializable) fotos);
                Image_Gallery_Fragment gallryFragment = Image_Gallery_Fragment.newInstance();
                gallryFragment.setArguments(result);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container, gallryFragment).addToBackStack(null).commit();
            }
        });

        videosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putSerializable("videos", (Serializable) videos);
                Image_Gallery_Fragment gallryFragment = Image_Gallery_Fragment.newInstance();
                gallryFragment.setArguments(result);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container, gallryFragment).addToBackStack(null).commit();
            }
        });

        btnAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getEditando().setValue(false);
                viewModel.getRevisando().setValue(false);
                getParentFragmentManager().popBackStackImmediate();
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Description_Ins_Fragment f = new Description_Ins_Fragment();
                Bundle args = new Bundle();
                args.putSerializable("usu", usuario);
                args.putSerializable("incidencia", incidencia);
                f.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container,  f).addToBackStack("rev_inc_ins").commit();
            }
        });
    }
}