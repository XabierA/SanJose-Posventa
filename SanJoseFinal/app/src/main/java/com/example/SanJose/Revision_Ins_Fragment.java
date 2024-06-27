package com.example.SanJose;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
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
import com.example.SanJose.Models.Email;
import com.example.SanJose.Models.GetLastInc;
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
import retrofit2.http.Query;


public class Revision_Ins_Fragment extends Fragment implements FragmentCallBack {
    private Context context;

    private InspeccionViewModel viewModel;

    private Inspeccion inspeccionActual;

    private User user;

    private Incidencia incidencia;

    private Boolean editandoIns, insEnviada;

    private String disciplina, especialidad, descripcion, complemento1, complemento2;

    private List<String> disciplinas = new ArrayList<String>();

    private ImageView plano;

    private Button revisarBtn, guardarIncBtn, enviarBtn, audiosBtn, fotosBtn, videosBtn;

    private ImageView remoteEye, zoomBtn;

    private TextView tvDisciplina, tvDescripcion, tvComplemento1, tvComplemento2;

    private File audio, foto, video, localizacion, planoFusion;

    private User usuario;

    private Proyecto proyecto;

    private int numIncidencias;

    private List<File> audios = new ArrayList<File>();
    private List<File> fotos = new ArrayList<File>();
    private List<File> videos = new ArrayList<File>();

    private Integer numTotalIncidencias;

    private String nombreInc;

    private String[] audionames, fotonames, videonames;

    private int codigoData;

    private int previousAudios, previousVideos, previousFotos;

    @Override
    public void onDataSent(File yourData) {
        localizacion = yourData;
    }


    private interface PostRequestInspeccion{
        @POST("crearInspeccion")
        Call<response> postInspeccion(@Body Inspeccion inspeccion);
    }

    /*private interface PostRequestInspeccion{
        @POST("crearInspeccion_testing")
        Call<response> postInspeccion(@Body Inspeccion inspeccion);
    }*/


    private interface PutUpdateNumIns{
        @PUT("updateNumIns")
        Call<UpdateNumInc> updateNumInc(@Body UpdateNumInc updateNumIns);
    }

    /*private interface PutUpdateNumIns{
        @PUT("updateNumIns_copy")
        Call<UpdateNumInc> updateNumInc(@Body UpdateNumInc updateNumIns);
    }*/

    private interface GetNumIns{
        @GET("getNumIns")
        Call<Integer> getNumIns(@Query("idProyecto") String idProyecto);
    }

    /*private interface GetNumIns{
        @GET("getNumIns_copy")
        Call<Integer> getNumIns(@Query("idProyecto") String idProyecto);
    }*/

    private interface GetTotalNumIns{
        @GET("GetTotalNumIns")
        Call<List<GetLastInc>> getTotalNumIns();
    }

    /*private interface GetTotalNumIns{
        @GET("GetTotalNumIns_copy")
        Call<List<GetLastInc>> getTotalNumIns();
    }*/

    public Revision_Ins_Fragment() {
        // Required empty public constructor
    }

    public static Revision_Ins_Fragment newInstance() {
        Revision_Ins_Fragment fragment = new Revision_Ins_Fragment();
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
        final Observer<Inspeccion> inspeccionObserver = new Observer<Inspeccion>() {
            @Override
            public void onChanged(Inspeccion inspeccion) {
                inspeccionActual = inspeccion;
            }
        };
        viewModel.getCurrentInspeccion().observe(getActivity(), inspeccionObserver);
        final Observer<Boolean> enviadaObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean enviada) {
                insEnviada = enviada;
            }
        };
        viewModel.getEnviada().observe(getActivity(), enviadaObserver);

        final Observer<Boolean> editandoObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean editando) {
                editandoIns = editando;
            }
        };
        viewModel.getEditando().observe(getActivity(), editandoObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_revision2, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        remoteEye = getView().findViewById(R.id.remoteeye_rev);
        zoomBtn = getView().findViewById(R.id.zoom_rev);
        revisarBtn = getView().findViewById(R.id.btnEditar_rev);
        guardarIncBtn = getView().findViewById(R.id.btnGuardar_rev);
        enviarBtn = getView().findViewById(R.id.btnEnviar_rev);
        audiosBtn = getView().findViewById(R.id.btnAudios_rev);
        fotosBtn = getView().findViewById(R.id.btnFotos_rev);
        videosBtn = getView().findViewById(R.id.btnVideos_rev);
        tvDisciplina = getView().findViewById(R.id.rev_disciplina);
        tvDescripcion = getView().findViewById(R.id.rev_descripcion);
        tvComplemento1 = getView().findViewById(R.id.rev_complemento1);
        tvComplemento2 = getView().findViewById(R.id.rev_complemento2);
        //plano = getView().findViewById(R.id.plano1et);
        incidencia = (Incidencia) getArguments().getSerializable("incidencia");

        Log.v("LLLLLLLLLEEEEGAAAAAA", "LELEELELELELELELELLELELE");

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://eu-west-2.aws.data.mongodb-api.com/app/apisanjose-iciuj/endpoint/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        GetNumIns requestPaciente = retrofit.create(GetNumIns.class);
        Call<Integer> call = requestPaciente.getNumIns(incidencia.proyecto);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Integer apiResponse = response.body();
                numIncidencias = apiResponse+1;
                Log.v("num", numIncidencias+"");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("Incidencias", t.getMessage());
                Toast.makeText(context, "no focuchiona", Toast.LENGTH_LONG).show();
            }
        });

        Revision_Ins_Fragment.GetTotalNumIns totalNumInc = retrofit.create(Revision_Ins_Fragment.GetTotalNumIns.class);
        Call<List<GetLastInc>> callTotalNumInc = totalNumInc.getTotalNumIns();
        callTotalNumInc.enqueue(new Callback<List<GetLastInc>>() {
            @Override
            public void onResponse(Call<List<GetLastInc>> call, Response<List<GetLastInc>> response) {
                GetLastInc apiResponse = new GetLastInc();
                if (response.body().size() > 0){
                    apiResponse = response.body().get(0);
                }else {
                    apiResponse.setCodigo("0");
                }
                Log.v("INTEGER", apiResponse.getCodigo());
                String number  = apiResponse.getCodigo().replaceAll("[^0-9]", "");
                numTotalIncidencias = Integer.parseInt(number)+1;
            }

            @Override
            public void onFailure(Call<List<GetLastInc>> call, Throwable t) {
                Log.e("Incidencias", t.getMessage());
                Toast.makeText(context, "no focuchiona", Toast.LENGTH_LONG).show();
            }
        });

        Random r = new Random();
        //int codigoInc = r.nextInt(999999999 - 1) + 1;
        codigoData = r.nextInt(900 - 1) + 1;

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.planofusiontest);

        try {
            File fffff =  persistImage(bitmap1, "plano1", context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(getArguments().getSerializable("audios") != null){
            audios = (List<File>) getArguments().getSerializable("audios");
            audionames = new String[audios.size()];
        }else {
            audios = null;
            audionames = new String[audios.size()];
        }
        if(getArguments().getSerializable("imagenes") != null){
            fotos = (List<File>) getArguments().getSerializable("imagenes");
            fotonames = new String[fotos.size()];
        }else {
            fotos = null;
            fotonames = new String[fotos.size()];
        }
        if(getArguments().getSerializable("videos") != null){
            videos = (List<File>) getArguments().getSerializable("videos");
            videonames = new String[videos.size()];
        }else {
            videos = null;
            videonames = new String[videos.size()];
        }
        ///////////////////////////////////////////////////////////////////////
        disciplina = getArguments().getString("disciplina");
        especialidad = getArguments().getString("especialidad");
        descripcion = getArguments().getString("descripcion");
        complemento1 = getArguments().getString("complemento1");
        complemento2 = getArguments().getString("complemento2");
        user = (User) getArguments().getSerializable("usu");


        disciplinas = incidencia.disciplinas;

        String textDisc = "";

        for (int i2 = 0; i2 < disciplinas.size(); i2++){
            if (i2 == disciplinas.size()-1){
                textDisc = textDisc+disciplinas.get(i2);
            }else {
                textDisc = textDisc+disciplinas.get(i2)+", ";
            }
        }

        tvDisciplina.setText(textDisc);
        tvDescripcion.setText(descripcion);
        tvComplemento1.setText(complemento1);
        tvComplemento2.setText(complemento2);


        ContextWrapper contextWrapper = new ContextWrapper(context);

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

        revisarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Inspeccion insRev = new Inspeccion();
                //incRev.setDisciplina(tvDisciplina.getText().toString());
                insRev.setDescripcion(descripcion);
                insRev.setComplemento1(complemento1);
                insRev.setComplemento2(complemento2);
                insRev.setDocumentos(new Documentos(audionames, fotonames, videonames));
                viewModel.getAudios().setValue(audios);
                viewModel.getFotos().setValue(fotos);
                viewModel.getVideos().setValue(videos);
                viewModel.getCurrentInspeccion().setValue(insRev);
                viewModel.getRevisando().setValue(true);
                FragmentManager fm = getParentFragmentManager();
                for (int i = fm.getBackStackEntryCount() - 1; i > 0; i--) {
                    if (!fm.getBackStackEntryAt(i).getName().equalsIgnoreCase("descripcion_ins")) {
                        fm.popBackStack();
                    }
                    else
                    {
                        break;
                    }
                }
                getParentFragmentManager().popBackStackImmediate();
            }
        });

        enviarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getEnviada().setValue(true);
                if (viewModel.getRevisando().getValue()){
                    viewModel.getRevisando().setValue(false);
                }
                if (audios != null){
                    UploadAmazonS3 s3_1 = new UploadAmazonS3();
                    s3_1.setName(numTotalIncidencias);
                    s3_1.setType("audio");
                    s3_1.setFormato("inspeccion");
                    if(editandoIns){
                        s3_1.setNumaudio(previousAudios);
                    }
                    s3_1.execute(audios);
                }
                if (fotos != null){
                    UploadAmazonS3 s3_2 = new UploadAmazonS3();
                    s3_2.setName(numTotalIncidencias);
                    s3_2.setType("foto");
                    s3_2.setFormato("inspeccion");
                    if(editandoIns){
                        s3_2.setNumfoto(previousFotos);
                    }
                    s3_2.execute(fotos);
                }
                if (videos != null){
                    UploadAmazonS3 s3_3 = new UploadAmazonS3();
                    s3_3.setName(numTotalIncidencias);
                    s3_3.setType("video");
                    s3_3.setFormato("inspeccion");
                    if(editandoIns){
                        s3_3.setNumvideo(previousVideos);
                    }
                    s3_3.execute(videos);
                }

                inspeccionActual = new Inspeccion();

                int length = (int) Math.log10(numTotalIncidencias) + 1;
                switch (length){
                    case 1:
                        inspeccionActual.setCodigo("INS00"+numTotalIncidencias);
                        break;
                    case 2:
                        inspeccionActual.setCodigo("INS0"+numTotalIncidencias);
                        break;
                    case 3:
                        inspeccionActual.setCodigo("INS"+numTotalIncidencias);
                }

                inspeccionActual.setAutor(((User)getArguments().getSerializable("usuario")).get_id());
                //inspeccionActual.setAutorNombre(((User)getArguments().getSerializable("usuario")).getNombre());

                Date c = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd''HH:mm:ss");
                String formattedDate = df.format(c);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //incidencia.setFecha(fecha.toString());
                    inspeccionActual.setFecha(new Date());
                }
                inspeccionActual.setFecha(new Date());
                inspeccionActual.setDescripcion(descripcion);
                inspeccionActual.setComplemento1(complemento1);
                inspeccionActual.setComplemento2(complemento2);
                inspeccionActual.setIncidencia(incidencia._id);
                inspeccionActual.setProyecto(incidencia.proyecto);

                if (audios != null || fotos != null || videos != null){
                    setAudioNames(audios);
                    setFotoNames(fotos);
                    setVideoNames(videos);
                    inspeccionActual.setDocumentos(new Documentos(audionames, fotonames, videonames));
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                Log.v("lllllllega2", "lleelellega2");
                builder.setMessage("Esta conforme con esta incidencia?")
                        .setPositiveButton("Conforme", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                inspeccionActual.setEstado("conforme");
                                Revision_Ins_Fragment.PostRequestInspeccion postInspeccion = retrofit.create(Revision_Ins_Fragment.PostRequestInspeccion.class);
                                Call<response> call = postInspeccion.postInspeccion(inspeccionActual);
                                call.enqueue(new Callback<response>() {
                                    @Override
                                    public void onResponse(Call<response> call, Response<response> response) {
                                        UpdateNumInc numInc = new UpdateNumInc(incidencia.proyecto, numIncidencias);
                                        Revision_Ins_Fragment.PutUpdateNumIns putUpdateNumInc = retrofit.create(Revision_Ins_Fragment.PutUpdateNumIns.class);
                                        Call<UpdateNumInc> call2 = putUpdateNumInc.updateNumInc(numInc);
                                        call2.enqueue(new Callback<UpdateNumInc>() {
                                            @Override
                                            public void onResponse(Call<UpdateNumInc> call2, Response<UpdateNumInc> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<UpdateNumInc> call2, Throwable t) {

                                            }
                                        });
                                        inspeccionActual.setObjectId(response.body().getInsertedId());
                                        viewModel.getCurrentInspeccion().setValue(inspeccionActual);
                                        ElegirGruposEmailFragment f = new ElegirGruposEmailFragment();
                                        getParentFragmentManager().beginTransaction().replace(R.id.nav_container,  f).addToBackStack("revision").commit();
                                    }

                                    @Override
                                    public void onFailure(Call<response> call, Throwable t) {
                                        Toast.makeText(context, "Error al guardar la incidencia", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No conforme", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                inspeccionActual.setEstado("no conforme");
                                Revision_Ins_Fragment.PostRequestInspeccion postInspeccion = retrofit.create(Revision_Ins_Fragment.PostRequestInspeccion.class);
                                Call<response> call = postInspeccion.postInspeccion(inspeccionActual);
                                call.enqueue(new Callback<response>() {
                                    @Override
                                    public void onResponse(Call<response> call, Response<response> response) {
                                        UpdateNumInc numInc = new UpdateNumInc(incidencia.proyecto, numIncidencias);
                                        Revision_Ins_Fragment.PutUpdateNumIns putUpdateNumInc = retrofit.create(Revision_Ins_Fragment.PutUpdateNumIns.class);
                                        Call<UpdateNumInc> call2 = putUpdateNumInc.updateNumInc(numInc);
                                        call2.enqueue(new Callback<UpdateNumInc>() {
                                            @Override
                                            public void onResponse(Call<UpdateNumInc> call2, Response<UpdateNumInc> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<UpdateNumInc> call2, Throwable t) {

                                            }
                                        });
                                        inspeccionActual.setObjectId(response.body().getInsertedId());
                                        viewModel.getCurrentInspeccion().setValue(inspeccionActual);
                                        ElegirGruposEmailFragment f = new ElegirGruposEmailFragment();
                                        getParentFragmentManager().beginTransaction().replace(R.id.nav_container,  f).addToBackStack("revision").commit();
                                    }

                                    @Override
                                    public void onFailure(Call<response> call, Throwable t) {
                                        Toast.makeText(context, "Error al guardar la incidencia", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();


            }
        });


        guardarIncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getEnviada().setValue(false);
                if (viewModel.getRevisando().getValue()){
                    viewModel.getRevisando().setValue(false);
                }

                if (audios != null){
                    UploadAmazonS3 s3_1 = new UploadAmazonS3();
                    s3_1.setName(numTotalIncidencias);
                    s3_1.setType("audio");
                    s3_1.setFormato("inspeccion");
                    s3_1.execute(audios);
                }
                if (fotos != null){
                    UploadAmazonS3 s3_2 = new UploadAmazonS3();
                    s3_2.setName(numTotalIncidencias);
                    s3_2.setType("foto");
                    s3_2.setFormato("inspeccion");
                    s3_2.execute(fotos);
                }
                if (videos != null){
                    UploadAmazonS3 s3_3 = new UploadAmazonS3();
                    s3_3.setName(numTotalIncidencias);
                    s3_3.setType("video");
                    s3_3.setFormato("inspeccion");
                    s3_3.execute(videos);
                }

                inspeccionActual = new Inspeccion();

                int length = (int) Math.log10(numTotalIncidencias) + 1;
                switch (length){
                    case 1:
                        inspeccionActual.setCodigo("INS00"+numTotalIncidencias);
                        break;
                    case 2:
                        inspeccionActual.setCodigo("INS0"+numTotalIncidencias);
                        break;
                    case 3:
                        inspeccionActual.setCodigo("INS"+numTotalIncidencias);
                }

                inspeccionActual.setAutor(((User)getArguments().getSerializable("usuario")).get_id());


                Date c = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd''HH:mm:ss");
                String formattedDate = df.format(c);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    inspeccionActual.setFecha(new Date());
                }
                inspeccionActual.setFecha(new Date());
                inspeccionActual.setDescripcion(descripcion);
                inspeccionActual.setComplemento1(complemento1);
                inspeccionActual.setComplemento2(complemento2);
                inspeccionActual.setIncidencia(incidencia._id);
                inspeccionActual.setProyecto(incidencia.proyecto);

                Log.v("id", incidencia._id);

                if (audios != null || fotos != null || videos != null){
                    setAudioNames(audios);
                    setFotoNames(fotos);
                    setVideoNames(videos);
                    inspeccionActual.setDocumentos(new Documentos(audionames, fotonames, videonames));
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Esta conforme con esta incidencia?")
                        .setPositiveButton("Conforme", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                inspeccionActual.setEstado("conforme");

                                Revision_Ins_Fragment.PostRequestInspeccion postInspeccion = retrofit.create(Revision_Ins_Fragment.PostRequestInspeccion.class);
                                Call<response> call = postInspeccion.postInspeccion(inspeccionActual);
                                call.enqueue(new Callback<response>() {
                                    @Override
                                    public void onResponse(Call<response> call, Response<response> response) {
                                        inspeccionActual.setObjectId(response.body().getInsertedId());
                                        viewModel.getCurrentInspeccion().setValue(inspeccionActual);
                                        UpdateNumInc numInc = new UpdateNumInc(incidencia.proyecto, numIncidencias);
                                        Revision_Ins_Fragment.PutUpdateNumIns putUpdateNumInc = retrofit.create(Revision_Ins_Fragment.PutUpdateNumIns.class);
                                        Call<UpdateNumInc> call2 = putUpdateNumInc.updateNumInc(numInc);
                                        call2.enqueue(new Callback<UpdateNumInc>() {
                                            @Override
                                            public void onResponse(Call<UpdateNumInc> call2, Response<UpdateNumInc> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<UpdateNumInc> call2, Throwable t) {

                                            }
                                        });

                                        FragmentManager fm = getParentFragmentManager();
                                        for (int i = fm.getBackStackEntryCount() - 1; i > 0; i--) {
                                            if (!fm.getBackStackEntryAt(i).getName().equalsIgnoreCase("main")) {
                                                fm.popBackStack();
                                            }
                                            else
                                            {
                                                break;
                                            }
                                        }
                                        getParentFragmentManager().popBackStackImmediate();
                                    }

                                    @Override
                                    public void onFailure(Call<response> call, Throwable t) {
                                        Log.e("Incidencia", t.getMessage());
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No conforme", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                inspeccionActual.setEstado("no conforme");
                                Revision_Ins_Fragment.PostRequestInspeccion postInspeccion = retrofit.create(Revision_Ins_Fragment.PostRequestInspeccion.class);
                                Call<response> call = postInspeccion.postInspeccion(inspeccionActual);
                                call.enqueue(new Callback<response>() {
                                    @Override
                                    public void onResponse(Call<response> call, Response<response> response) {
                                        inspeccionActual.setObjectId(response.body().getInsertedId());
                                        viewModel.getCurrentInspeccion().setValue(inspeccionActual);
                                        UpdateNumInc numInc = new UpdateNumInc(incidencia.proyecto, numIncidencias);
                                        Revision_Ins_Fragment.PutUpdateNumIns putUpdateNumInc = retrofit.create(Revision_Ins_Fragment.PutUpdateNumIns.class);
                                        Call<UpdateNumInc> call2 = putUpdateNumInc.updateNumInc(numInc);
                                        call2.enqueue(new Callback<UpdateNumInc>() {
                                            @Override
                                            public void onResponse(Call<UpdateNumInc> call2, Response<UpdateNumInc> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<UpdateNumInc> call2, Throwable t) {

                                            }
                                        });

                                        FragmentManager fm = getParentFragmentManager();
                                        for (int i = fm.getBackStackEntryCount() - 1; i > 0; i--) {
                                            if (!fm.getBackStackEntryAt(i).getName().equalsIgnoreCase("main")) {
                                                fm.popBackStack();
                                            }
                                            else
                                            {
                                                break;
                                            }
                                        }
                                        getParentFragmentManager().popBackStackImmediate();
                                    }

                                    @Override
                                    public void onFailure(Call<response> call, Throwable t) {
                                        Log.e("Incidencia", t.getMessage());
                                    }
                                });
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();
            }
        });
    }


    private File persistImage(Bitmap bitmap, String name, Context context) throws IOException {
        File filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                name,  /* prefix */
                ".jpg",         /* suffix */
                filesDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            bitmap.eraseColor(Color.WHITE);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "Error writing bitmap", e);
        }
        planoFusion = imageFile;
        return imageFile;
    }

    private void setAudioNames(List<File> audios){
        for (int i = 0; i < audios.size(); i++){
            int num = i+1;
            /*if (editandoInc){
                num = i+1+previousAudios;
            }*/
            audionames[i] = "audio-INS"+numTotalIncidencias+"-"+num+".mp4";
        }
    }

    private void setFotoNames(List<File> fotos){
        for (int i = 0; i < fotos.size(); i++){
            int num = i+1;
            /*if (editandoInc){
                num = i+1+previousFotos;
            }*/
            fotonames[i] = "foto-INS"+numTotalIncidencias+"-"+num+".jpg";
        }
    }

    private void setVideoNames(List<File> videos){
        for (int i = 0; i < videos.size(); i++){
            int num = i+1;
            /*if (editandoInc){
                num = i+1+previousVideos;
            }*/
            videonames[i] = "video-INS"+numTotalIncidencias+"-"+num+".mp4";
        }
    }
}