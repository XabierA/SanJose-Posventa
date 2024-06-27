package com.example.SanJose;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

import com.example.SanJose.FragmentCallBack.FragmentCallBack;
import com.example.SanJose.Models.Documentos;
import com.example.SanJose.Models.GetLastInc;
import com.example.SanJose.Models.Incidencia;
import com.example.SanJose.Models.Localizacion;
import com.example.SanJose.Models.Proyecto;
import com.example.SanJose.Models.UpdateNumInc;
import com.example.SanJose.Models.User;
import com.example.SanJose.Models.response;
import com.example.SanJose.ViewModels.Image_Gallery_Fragment;
import com.example.SanJose.ViewModels.IncidenciaViewModel;

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


public class RevisionFragment extends Fragment implements FragmentCallBack {
    private Context context;

    private IncidenciaViewModel viewModel;

    private Incidencia incidenciaActual;

    private Boolean editandoInc, incEnviada;

    private String disciplina, especialidad, descripcion, complemento1, complemento2;

    private List<String> disciplinas = new ArrayList<String>();

    private ImageView plano;

    private Button revisarBtn, guardarIncBtn, enviarBtn, audiosBtn, fotosBtn, videosBtn;

    private ImageView remoteEye, zoomBtn;

    private TextView tvDisciplina, tvDescripcion, tvComplemento1, tvComplemento2;

    private File audio, foto, video, localizacion, planoFusion;

    private User usuario;

    private Proyecto proyecto;

    private int numIncidencias, numTotalIncidencias;

    private List<File> audios = new ArrayList<File>();
    private List<File> fotos = new ArrayList<File>();
    private List<File> videos = new ArrayList<File>();

    private String nombreInc;

    private String[] audionames, fotonames, videonames;

    private int codigoData;

    private int previousAudios, previousVideos, previousFotos;

    @Override
    public void onDataSent(File yourData) {
        localizacion = yourData;
    }


    private interface PostRequestIncidencia{
        @POST("crearIncidencia")
        Call<response> postIncidencia(@Body Incidencia incidencia);
    }

    /*private interface PostRequestIncidencia{
        @POST("crearIncidencia_testing")
        Call<response> postIncidencia(@Body Incidencia incidencia);
    }*/

    private interface PutUpdateIncidencia{
        @PUT("updateIncidencia")
        Call<Incidencia> updateIncidencia(@Body Incidencia incidencia);
    }

    private interface PutUpdateNumInc{
        @PUT("updateNumInc")
        Call<UpdateNumInc> updateNumInc(@Body UpdateNumInc updateNumInc);
    }

    /*private interface PutUpdateNumInc{
        @PUT("updateNumInc_copy")
        Call<UpdateNumInc> updateNumInc(@Body UpdateNumInc updateNumInc);
    }*/

    private interface GetNumInc{
        @GET("GetNumInc")
        Call<Integer> getNumInc(@Query("idProyecto") String idProyecto);
    }

    /*private interface GetNumInc{
        @GET("GetNumInc_copy")
        Call<Integer> getNumInc(@Query("idProyecto") String idProyecto);
    }*/

    private interface GetTotalNumInc{
        @GET("GetTotalNumInc")
        Call<List<GetLastInc>> getTotalNumInc();
    }

    /*private interface GetTotalNumInc{
        @GET("GetTotalNumInc_copy")
        Call<List<GetLastInc>> getTotalNumInc();
    }*/

    public RevisionFragment() {
        // Required empty public constructor
    }

    public static RevisionFragment newInstance() {
        RevisionFragment fragment = new RevisionFragment();
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
        viewModel = new ViewModelProvider(getActivity()).get(IncidenciaViewModel.class);
        final Observer<Boolean> editandoObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean editando) {
                editandoInc = editando;
            }
        };
        viewModel.getEditando().observe(getActivity(), editandoObserver);

        final Observer<Boolean> enviadaObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean enviada) {
                incEnviada = enviada;
            }
        };
        viewModel.getEnviada().observe(getActivity(), enviadaObserver);
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

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://eu-west-2.aws.data.mongodb-api.com/app/apisanjose-iciuj/endpoint/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        GetNumInc requestPaciente = retrofit.create(GetNumInc.class);
        Call<Integer> call = requestPaciente.getNumInc(((Proyecto)getArguments().getSerializable("proyecto")).get_id());
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Integer apiResponse = response.body();
                if (editandoInc){
                    numIncidencias = apiResponse;
                }else {
                    numIncidencias = apiResponse+1;
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("Incidencias", t.getMessage());
                Toast.makeText(context, "no focuchiona", Toast.LENGTH_LONG).show();
            }
        });

        GetTotalNumInc totalNumInc = retrofit.create(GetTotalNumInc.class);
        Call<List<GetLastInc>> callTotalNumInc = totalNumInc.getTotalNumInc();
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
                if (editandoInc){
                    numTotalIncidencias = Integer.parseInt(number);
                }else {
                    numTotalIncidencias = Integer.parseInt(number)+1;
                }
                Log.v("INTEGER", numTotalIncidencias+"");
            }

            @Override
            public void onFailure(Call<List<GetLastInc>> call, Throwable t) {
                Log.e("Incidencias", t.getMessage());
                Toast.makeText(context, "no focuchiona", Toast.LENGTH_LONG).show();
            }
        });


        Random r = new Random();
        codigoData = r.nextInt(900 - 1) + 1;

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.planofusiontest);

        try {
            File fffff =  persistImage(bitmap1, "plano1", context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Locate_Fragment lf = new Locate_Fragment().newInstance();
        lf.setFragmentCallback(this);

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
        disciplinas = (List<String>) getArguments().getSerializable("disciplinas");
        especialidad = getArguments().getString("especialidad");
        descripcion = getArguments().getString("descripcion");
        complemento1 = getArguments().getString("complemento1");
        complemento2 = getArguments().getString("complemento2");

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

        if (editandoInc){
            guardarIncBtn.setText("Actualizar Incidencia");
            if (incidenciaActual.documentos.audios != null){
                if (incidenciaActual.documentos.audios.length > 0) {
                    String rawNumbers = incidenciaActual.documentos.audios[0].replaceAll("\\D+", "");
                    codigoData = Integer.parseInt(rawNumbers.substring(0, 3));
                    previousAudios = incidenciaActual.documentos.audios.length;
                    Log.v("previousaudios", previousAudios + "");
                }
            }else {
                previousAudios = 0;
            }
            if(incidenciaActual.documentos.fotos!= null){
                if(incidenciaActual.documentos.fotos.length > 0){
                    previousFotos = incidenciaActual.documentos.fotos.length;
                    if (incidenciaActual.documentos.audios.length == 0){
                        String rawNumbers = incidenciaActual.documentos.fotos[0].replaceAll("\\D+","");
                        codigoData = Integer.parseInt(rawNumbers.substring(0,3));
                    }
                }
            }else {
                previousFotos = 0;
            }
            if(incidenciaActual.documentos.videos != null){
                if(incidenciaActual.documentos.videos.length > 0){
                    previousVideos = incidenciaActual.documentos.videos.length;
                    if (incidenciaActual.documentos.audios.length == 0){
                        String rawNumbers = incidenciaActual.documentos.videos[0].replaceAll("\\D+","");
                        codigoData = Integer.parseInt(rawNumbers.substring(0,3));
                    }
                }
            }else {
                previousVideos = 0;
            }

        }

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
                Incidencia incRev = new Incidencia();
                incRev.setDisciplinas(disciplinas);
                incRev.setDescripcion(descripcion);
                incRev.setComplemento1(complemento1);
                incRev.setComplemento2(complemento2);
                incRev.setDocumentos(new Documentos(audionames, fotonames, videonames));
                viewModel.getAudios().setValue(audios);
                viewModel.getFotos().setValue(fotos);
                viewModel.getVideos().setValue(videos);
                viewModel.getCurrentIncidencia().setValue(incRev);
                viewModel.getRevisando().setValue(true);
                FragmentManager fm = getParentFragmentManager();
                for (int i = fm.getBackStackEntryCount() - 1; i > 0; i--) {
                    if (!fm.getBackStackEntryAt(i).getName().equalsIgnoreCase("disciplina")) {
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
                    s3_1.setFormato("incidencia");
                    if(editandoInc){
                        s3_1.setNumaudio(previousAudios);
                    }
                    s3_1.execute(audios);
                }
                if (fotos != null){
                    UploadAmazonS3 s3_2 = new UploadAmazonS3();
                    s3_2.setName(numTotalIncidencias);
                    s3_2.setType("foto");
                    s3_2.setFormato("incidencia");
                    if(editandoInc){
                        s3_2.setNumfoto(previousFotos);
                    }
                    s3_2.execute(fotos);
                }
                if (videos != null){
                    UploadAmazonS3 s3_3 = new UploadAmazonS3();
                    s3_3.setName(numTotalIncidencias);
                    s3_3.setType("video");
                    s3_3.setFormato("incidencia");
                    if(editandoInc){
                        s3_3.setNumvideo(previousVideos);
                    }
                    s3_3.execute(videos);
                }
                if (localizacion != null || planoFusion != null){
                    UploadAmazonS3 s3_4 = new UploadAmazonS3();
                    s3_4.setName(numTotalIncidencias);
                    s3_4.setType("plano");
                    s3_4.execute(new File[]{localizacion, planoFusion});
                }

                incidenciaActual = new Incidencia();

                int length = (int) Math.log10(numTotalIncidencias) + 1;
                switch (length){
                    case 1:
                        incidenciaActual.setCodigo("INC00"+numTotalIncidencias);
                        break;
                    case 2:
                        incidenciaActual.setCodigo("INC0"+numTotalIncidencias);
                        break;
                    case 3:
                        incidenciaActual.setCodigo("INC"+numTotalIncidencias);
                }

                incidenciaActual.setAutor(((User)getArguments().getSerializable("usuario")).get_id());
                incidenciaActual.setAutorNombre(((User)getArguments().getSerializable("usuario")).getNombre());

                Date c = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd''HH:mm:ss");
                String formattedDate = df.format(c);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    incidenciaActual.setFecha(new Date());
                }
                incidenciaActual.setFecha(new Date());
                incidenciaActual.setProyecto(((Proyecto)getArguments().getSerializable("proyecto")).get_id());

                //incidenciaActual.setDisciplina(disciplina);
                incidenciaActual.setDisciplinas(disciplinas);
                incidenciaActual.setDescripcion(descripcion);
                incidenciaActual.setComplemento1(complemento1);
                incidenciaActual.setComplemento2(complemento2);
                incidenciaActual.setEstado("creada");


                if (audios != null || fotos != null || videos != null){
                    setAudioNames(audios);
                    setFotoNames(fotos);
                    setVideoNames(videos);
                    incidenciaActual.setDocumentos(new Documentos(audionames, fotonames, videonames));
                }

                if (localizacion != null){
                    incidenciaActual.setLocalizacion(new Localizacion("coordenadas"+numIncidencias+".mp3", "plano"+numIncidencias+".jpg"));
                }
                if (editandoInc){

                    viewModel.getEditando().setValue(false);

                    incidenciaActual.setObjectId(viewModel.getCurrentIncidencia().getValue()._id);
                    RevisionFragment.PutUpdateIncidencia putUpdateIncidencia = retrofit.create(RevisionFragment.PutUpdateIncidencia.class);
                    Call<Incidencia> call = putUpdateIncidencia.updateIncidencia(incidenciaActual);
                    call.enqueue(new Callback<Incidencia>() {
                        @Override
                        public void onResponse(Call<Incidencia> call, Response<Incidencia> response) {
                            Log.v("TEST", "incidencia actualizada");
                            viewModel.getCurrentIncidencia().setValue(incidenciaActual);
                            ElegirGruposEmailFragment f = new ElegirGruposEmailFragment();
                            getParentFragmentManager().beginTransaction().replace(R.id.nav_container,  f).addToBackStack("revision").commit();
                        }

                        @Override
                        public void onFailure(Call<Incidencia> call, Throwable t) {
                            Log.v("TEST2", "Error al actualizar");
                        }
                    });
                }else {
                    RevisionFragment.PostRequestIncidencia postIncidencia = retrofit.create(RevisionFragment.PostRequestIncidencia.class);
                    Call<response> call = postIncidencia.postIncidencia(incidenciaActual);
                    call.enqueue(new Callback<response>() {
                        @Override
                        public void onResponse(Call<response> call, Response<response> response) {
                            UpdateNumInc numInc = new UpdateNumInc(((Proyecto)getArguments().getSerializable("proyecto")).get_id(), numIncidencias);
                            RevisionFragment.PutUpdateNumInc putUpdateNumInc = retrofit.create(RevisionFragment.PutUpdateNumInc.class);
                            Call<UpdateNumInc> call2 = putUpdateNumInc.updateNumInc(numInc);
                            call2.enqueue(new Callback<UpdateNumInc>() {
                                @Override
                                public void onResponse(Call<UpdateNumInc> call2, Response<UpdateNumInc> response) {

                                }

                                @Override
                                public void onFailure(Call<UpdateNumInc> call2, Throwable t) {

                                }
                            });
                            incidenciaActual.setObjectId(response.body().getInsertedId());
                            viewModel.getCurrentIncidencia().setValue(incidenciaActual);
                            ElegirGruposEmailFragment f = new ElegirGruposEmailFragment();
                            getParentFragmentManager().beginTransaction().replace(R.id.nav_container,  f).addToBackStack("revision").commit();
                        }

                        @Override
                        public void onFailure(Call<response> call, Throwable t) {
                            Toast.makeText(context, "Error al guardar la incidencia", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
                    s3_1.setFormato("incidencia");
                    if(editandoInc){
                        s3_1.setNumaudio(previousAudios);
                    }
                    s3_1.execute(audios);
                }
                if (fotos != null){
                    UploadAmazonS3 s3_2 = new UploadAmazonS3();
                    s3_2.setName(numTotalIncidencias);
                    s3_2.setType("foto");
                    s3_2.setFormato("incidencia");
                    if(editandoInc){
                        s3_2.setNumfoto(previousFotos);
                    }
                    s3_2.execute(fotos);
                }
                if (videos != null){
                    UploadAmazonS3 s3_3 = new UploadAmazonS3();
                    s3_3.setName(numTotalIncidencias);
                    s3_3.setType("video");
                    s3_3.setFormato("incidencia");
                    if(editandoInc){
                        s3_3.setNumvideo(previousVideos);
                    }
                    s3_3.execute(videos);
                }
                if (localizacion != null || planoFusion != null){
                    UploadAmazonS3 s3_4 = new UploadAmazonS3();
                    s3_4.setName(numTotalIncidencias);
                    s3_4.setType("plano");
                    s3_4.execute(new File[]{localizacion, planoFusion});
                }

                incidenciaActual = new Incidencia();

                int length = (int) Math.log10(numTotalIncidencias) + 1;
                switch (length){
                    case 1:
                        incidenciaActual.setCodigo("INC00"+numTotalIncidencias);
                        break;
                    case 2:
                        incidenciaActual.setCodigo("INC0"+numTotalIncidencias);
                        break;
                    case 3:
                        incidenciaActual.setCodigo("INC"+numTotalIncidencias);
                }
                incidenciaActual.setAutor(((User)getArguments().getSerializable("usuario")).get_id());
                incidenciaActual.setAutorNombre(((User)getArguments().getSerializable("usuario")).getNombre());


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    incidenciaActual.setFecha(new Date());
                }
                incidenciaActual.setFecha(new Date());
                incidenciaActual.setProyecto(((Proyecto)getArguments().getSerializable("proyecto")).get_id());
                incidenciaActual.setDisciplinas(disciplinas);
                incidenciaActual.setDescripcion(descripcion);
                incidenciaActual.setComplemento1(complemento1);
                incidenciaActual.setComplemento2(complemento2);
                incidenciaActual.setEstado("creada");

                if (audios != null || fotos != null || videos != null){
                    setAudioNames(audios);
                    setFotoNames(fotos);
                    setVideoNames(videos);
                    incidenciaActual.setDocumentos(new Documentos(audionames, fotonames, videonames));
                }
                if (localizacion != null){
                    incidenciaActual.setLocalizacion(new Localizacion("coordenadas"+numIncidencias+".mp3", "plano"+numIncidencias+".jpg"));
                }
                if (editandoInc){
                    viewModel.getEditando().setValue(false);
                    incidenciaActual.setObjectId(viewModel.getCurrentIncidencia().getValue()._id);
                    RevisionFragment.PutUpdateIncidencia putUpdateIncidencia = retrofit.create(RevisionFragment.PutUpdateIncidencia.class);
                    Call<Incidencia> call = putUpdateIncidencia.updateIncidencia(incidenciaActual);
                    call.enqueue(new Callback<Incidencia>() {
                        @Override
                        public void onResponse(Call<Incidencia> call, Response<Incidencia> response) {
                            Log.v("TEST", "incidencia actualizada");
                            viewModel.getCurrentIncidencia().setValue(incidenciaActual);
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
                        public void onFailure(Call<Incidencia> call, Throwable t) {
                            Log.v("TEST2", "Error al actualizar");
                        }
                    });
                }else {
                    RevisionFragment.PostRequestIncidencia postIncidencia = retrofit.create(RevisionFragment.PostRequestIncidencia.class);
                    Call<response> call = postIncidencia.postIncidencia(incidenciaActual);
                    call.enqueue(new Callback<response>() {
                        @Override
                        public void onResponse(Call<response> call, Response<response> response) {
                            incidenciaActual.setObjectId(response.body().getInsertedId());
                            viewModel.getCurrentIncidencia().setValue(incidenciaActual);
                            UpdateNumInc numInc = new UpdateNumInc(((Proyecto)getArguments().getSerializable("proyecto")).get_id(), numIncidencias);
                            RevisionFragment.PutUpdateNumInc putUpdateNumInc = retrofit.create(RevisionFragment.PutUpdateNumInc.class);
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
            }
        });
    }


    private File persistImage(Bitmap bitmap, String name, Context context) throws IOException {
        File filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //Toast.makeText(context, storageDir+"", Toast.LENGTH_LONG).show();
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
            if (editandoInc){
                num = i+1+previousAudios;
            }
            audionames[i] = "audio"+numTotalIncidencias+"-"+num+".mp4";
        }
    }

    private void setFotoNames(List<File> fotos){
        for (int i = 0; i < fotos.size(); i++){
            int num = i+1;
            if (editandoInc){
                num = i+1+previousFotos;
            }
            fotonames[i] = "foto"+numTotalIncidencias+"-"+num+".jpg";
        }
    }

    private void setVideoNames(List<File> videos){
        for (int i = 0; i < videos.size(); i++){
            int num = i+1;
            if (editandoInc){
                num = i+1+previousVideos;
            }
            videonames[i] = "video"+numTotalIncidencias+"-"+num+".mp4";
        }
    }
}