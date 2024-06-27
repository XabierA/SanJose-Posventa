package com.example.SanJose;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.SanJose.FragmentCallBack.FragmentCallBack;
import com.example.SanJose.Models.Documentos;
import com.example.SanJose.Models.Incidencia;
import com.example.SanJose.Models.Localizacion;
import com.example.SanJose.Models.Proyecto;
import com.example.SanJose.Models.User;
import com.example.SanJose.Models.response;
import com.example.SanJose.ViewModels.IncidenciaViewModel;
import com.example.SanJose.databinding.FragmentPlanoBinding;
import com.sun.mail.iap.Argument;

import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Time;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public class Plano_Fragment extends Fragment implements FragmentCallBack {
    private Context context;

    private IncidenciaViewModel viewModel;

    private Incidencia incidenciaActual;

    private Boolean revisandoInc;

    private String disciplina, especialidad, descripcion;

    private ImageView plano;

    private Button anteriorBtn, guardarIncBtn, plano1, plano2, plano3, plano4, plano5;

    private ImageView remoteEye, zoomBtn;

    private File audio, foto, video, localizacion, planoFusion;

    private User usuario;

    private Proyecto proyecto;

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

    private interface PutUpdateIncidencia{
        @PUT("updateIncidencia")
        Call<Incidencia> updateIncidencia(@Body Incidencia incidencia);
    }
    public Plano_Fragment() {
        // Required empty public constructor
    }

    public static Plano_Fragment newInstance() {
        Plano_Fragment fragment = new Plano_Fragment();
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
        final Observer<Boolean> revisandoObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean revisando) {
                revisandoInc = revisando;
            }
        };
        viewModel.getEditando().observe(getActivity(), revisandoObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plano_, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        remoteEye = getView().findViewById(R.id.remoteeye_plano);
        zoomBtn = getView().findViewById(R.id.zoom_plano);
        guardarIncBtn = getView().findViewById(R.id.btnGuardarIncidencia);
        anteriorBtn = getView().findViewById(R.id.btnAnterior_plano);
        plano = getView().findViewById(R.id.plano1et);
        plano1 = getView().findViewById(R.id.btnPlano1);
        plano2 = getView().findViewById(R.id.btnPlano2);
        plano3 = getView().findViewById(R.id.btnPlano3);
        plano4 = getView().findViewById(R.id.btnPlano4);
        plano5 = getView().findViewById(R.id.btnPlano5);


        Random r = new Random();
        int codigoInc = r.nextInt(999999999 - 1) + 1;
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
        especialidad = getArguments().getString("especialidad");
        descripcion = getArguments().getString("descripcion");

        if (revisandoInc){
            guardarIncBtn.setText("Actualizar Incidencia");
            if (incidenciaActual.documentos.audios.length > 0){
                String rawNumbers = incidenciaActual.documentos.audios[0].replaceAll("\\D+","");
                codigoData = Integer.parseInt(rawNumbers.substring(0,3));
                previousAudios = incidenciaActual.documentos.audios.length;
                Log.v("previousaudios", previousAudios+"");
            }else {
                previousAudios = 0;
            }
            if(incidenciaActual.documentos.fotos.length > 0){
                previousFotos = incidenciaActual.documentos.fotos.length;
                if (incidenciaActual.documentos.audios.length == 0){
                    String rawNumbers = incidenciaActual.documentos.fotos[0].replaceAll("\\D+","");
                    codigoData = Integer.parseInt(rawNumbers.substring(0,3));
                }
            }else {
                previousFotos = 0;
            }
            if(incidenciaActual.documentos.videos.length > 0){
                previousVideos = incidenciaActual.documentos.videos.length;
                if (incidenciaActual.documentos.audios.length == 0 && incidenciaActual.documentos.fotos.length > 0){
                    String rawNumbers = incidenciaActual.documentos.videos[0].replaceAll("\\D+","");
                    codigoData = Integer.parseInt(rawNumbers.substring(0,3));
                }
            }else {
                previousVideos = 0;
            }

        }

        ContextWrapper contextWrapper = new ContextWrapper(context);
        File screenshotDirecotry = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        BitmapDrawable drawable = (BitmapDrawable) plano.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        File f = new File(bitmap.toString(), "test1");

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

        anteriorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStackImmediate();
            }
        });

        plano1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container, lf).addToBackStack(null).commit();
            }
        });

        plano2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container, lf).addToBackStack(null).commit();
            }
        });

        plano3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container, lf).addToBackStack(null).commit();
            }
        });

        plano4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container, lf).addToBackStack(null).commit();
            }
        });

        plano5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container, lf).addToBackStack(null).commit();
            }
        });

        guardarIncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audios != null){
                    UploadAmazonS3 s3_1 = new UploadAmazonS3();
                    s3_1.setName(codigoData);
                    s3_1.setType("audio");
                    if(revisandoInc){
                        s3_1.setNumaudio(previousAudios);
                    }
                    s3_1.execute(audios);
                }
                if (fotos != null){
                    UploadAmazonS3 s3_2 = new UploadAmazonS3();
                    s3_2.setName(codigoData);
                    s3_2.setType("foto");
                    if(revisandoInc){
                        s3_2.setNumfoto(previousFotos);
                    }
                    s3_2.execute(fotos);
                }
                if (videos != null){
                    UploadAmazonS3 s3_3 = new UploadAmazonS3();
                    s3_3.setName(codigoData);
                    s3_3.setType("video");
                    if(revisandoInc){
                        s3_3.setNumvideo(previousVideos);
                    }
                    s3_3.execute(videos);
                }
                if (localizacion != null || planoFusion != null){
                    UploadAmazonS3 s3_4 = new UploadAmazonS3();
                    s3_4.setName(codigoData);
                    s3_4.setType("plano");
                    s3_4.execute(new File[]{localizacion, planoFusion});
                }

                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://eu-west-2.aws.data.mongodb-api.com/app/apisanjose-iciuj/endpoint/")
                        .addConverterFactory(GsonConverterFactory.create()).build();

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd''HH:mm:ss");

                incidenciaActual = new Incidencia();

                //incidencia.setCodigo(codigoInc.getText().toString());
                incidenciaActual.setCodigo(codigoInc+"");
                incidenciaActual.setAutor(((User)getArguments().getSerializable("usuario")).get_id());
                incidenciaActual.setAutorNombre(((User)getArguments().getSerializable("usuario")).getNombre());

                Date c = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd''HH:mm:ss");
                String formattedDate = df.format(c);
                Date currdate;
                LocalDateTime fecha = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    fecha = LocalDateTime.now();
                }
                try {
                    currdate = df.parse(formattedDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //incidencia.setFecha(fecha.toString());
                    incidenciaActual.setFecha(new Date());
                }
                incidenciaActual.setFecha(new Date());
                incidenciaActual.setProyecto(((Proyecto)getArguments().getSerializable("proyecto")).get_id());
                //incidencia.setProyecto(new ObjectId("6500387a45c3ad46a85f1002"));
                incidenciaActual.setDisciplina(disciplina);
                incidenciaActual.setDescripcion(descripcion);
                incidenciaActual.setEstado("creada");

                if (audios != null || fotos != null || videos != null){
                    setAudioNames(audios);
                    setFotoNames(fotos);
                    setVideoNames(videos);
                    incidenciaActual.setDocumentos(new Documentos(audionames, fotonames, videonames));
                }

                if (localizacion != null){
                    incidenciaActual.setLocalizacion(new Localizacion("coordenadas"+codigoData+".mp3", "plano"+codigoData+".jpg"));
                }
                if (revisandoInc){
                    viewModel.getEditando().setValue(false);
                    incidenciaActual.setObjectId(viewModel.getCurrentIncidencia().getValue()._id);
                    PutUpdateIncidencia putUpdateIncidencia = retrofit.create(PutUpdateIncidencia.class);
                    Call<Incidencia> call = putUpdateIncidencia.updateIncidencia(incidenciaActual);
                    call.enqueue(new Callback<Incidencia>() {
                        @Override
                        public void onResponse(Call<Incidencia> call, Response<Incidencia> response) {
                            Log.v("TEST", "incidencia actualizada");
                            viewModel.getCurrentIncidencia().setValue(incidenciaActual);
                            DialogFragment dialogFragment = new com.example.SanJose.DialogFragment();
                            Bundle args = new Bundle();
                            args.putSerializable("incidencia", incidenciaActual);
                            args.putSerializable("proyecto", getArguments().getSerializable("proyecto"));
                            dialogFragment.setArguments(args);
                            dialogFragment.show(getParentFragmentManager(), "DialogFragment");
                        }

                        @Override
                        public void onFailure(Call<Incidencia> call, Throwable t) {
                            Log.v("TEST2", "Error al actualizar");
                        }
                    });
                }else {
                    PostRequestIncidencia postIncidencia = retrofit.create(PostRequestIncidencia.class);
                    Call<response> call = postIncidencia.postIncidencia(incidenciaActual);
                    call.enqueue(new Callback<response>() {
                        @Override
                        public void onResponse(Call<response> call, Response<response> response) {
                            Log.v("test", response.body().getInsertedId());
                            incidenciaActual.setObjectId(response.body().getInsertedId());
                            viewModel.getCurrentIncidencia().setValue(incidenciaActual);
                            DialogFragment dialogFragment = new com.example.SanJose.DialogFragment();
                            Bundle args = new Bundle();
                            args.putSerializable("incidencia", incidenciaActual);
                            args.putSerializable("proyecto", getArguments().getSerializable("proyecto"));
                            dialogFragment.setArguments(args);
                            dialogFragment.show(getParentFragmentManager(), "DialogFragment");
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
            if (revisandoInc){
                num = i+1+previousAudios;
            }
            audionames[i] = "audio"+codigoData+"-"+num+".mp4";
        }
    }

    private void setFotoNames(List<File> fotos){
        for (int i = 0; i < fotos.size(); i++){
            int num = i+1;
            if (revisandoInc){
                num = i+1+previousFotos;
            }
            fotonames[i] = "foto"+codigoData+"-"+num+".jpg";
        }
    }

    private void setVideoNames(List<File> videos){
        for (int i = 0; i < videos.size(); i++){
            int num = i+1;
            if (revisandoInc){
                num = i+1+previousVideos;
            }
            videonames[i] = "video"+codigoData+"-"+num+".mp4";
        }
    }
}