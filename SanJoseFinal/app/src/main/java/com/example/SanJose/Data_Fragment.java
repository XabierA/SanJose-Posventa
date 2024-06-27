package com.example.SanJose;

import static android.app.Activity.RESULT_OK;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.SanJose.Models.Incidencia;
import com.example.SanJose.ViewModels.IncidenciaViewModel;
import com.example.SanJose.databinding.FragmentDataBinding;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class Data_Fragment extends Fragment {
    private static int MICROPHONE_PERMISSION_CODE = 200;
    private Context context;

    private IncidenciaViewModel viewModel;

    private Boolean revisandoInc;

    private String disciplina, especialidad, descripcion, complemento1, complemento2;

    private List<String> disciplinas = new ArrayList<String>();

    MediaRecorder mediaRecorder;

    private ImageView remoteEyeBtn, zoomBtn;

    private Button audioBtn, videoBtn, fotoBtn, anteriorBtn, siguienteBtn;

    private List<File> audios = new ArrayList<File>();

    private List<File> imagenes = new ArrayList<File>();

    private List<File> videos = new ArrayList<File>();

    private boolean grabando = false;

    private int numAudioInc = 0;

    public Data_Fragment() {
        // Required empty public constructor
    }

    public static Data_Fragment newInstance() {
        Data_Fragment fragment = new Data_Fragment();
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
        final Observer<Boolean> revisandoObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean revisando) {
                revisandoInc = revisando;
            }
        };
        viewModel.getRevisando().observe(getActivity(), revisandoObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        remoteEyeBtn = getView().findViewById(R.id.remoteeye_data);
        zoomBtn = getView().findViewById(R.id.zoom_data);
        audioBtn = getView().findViewById(R.id.audioBtn);
        videoBtn = getView().findViewById(R.id.videoBtn);
        fotoBtn = getView().findViewById(R.id.fotoBtn);
        anteriorBtn = getView().findViewById(R.id.btnAnterior_Docs);
        siguienteBtn = getView().findViewById(R.id.btnSiguiente_Docs);

        Log.v("asd", revisandoInc+"");

        if (revisandoInc){
            audios =  viewModel.getAudios().getValue();
            imagenes = viewModel.getFotos().getValue();
            videos = viewModel.getVideos().getValue();
        }

        if(getArguments().getString("disciplina") != null){
            disciplina = getArguments().getString("disciplina");
        }if(getArguments().getString("descripcion") != null){
            descripcion = getArguments().getString("descripcion");
        }if(getArguments().getString("complemento1") != null){
            complemento1 = getArguments().getString("complemento1");
        }if(getArguments().getString("complemento2") != null){
            complemento2 = getArguments().getString("complemento2");
        }if(getArguments().getString("especialidad") != null){
            especialidad = getArguments().getString("especialidad");
        }if (getArguments().getSerializable("disciplinas") != null){
            disciplinas = (List<String>) getArguments().getSerializable("disciplinas");
        }

        getMicrophonePermission();

        if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    android.Manifest.permission.CAMERA}, 101);
        }else {

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

        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(grabando == false){
                    audioBtn.setBackground(getResources().getDrawable(R.drawable.custom_doc_click_btn));
                    btnRecordPressed();
                    grabando = true;
                }else{
                    audioBtn.setBackground(getResources().getDrawable(R.drawable.custom_doc_btn));
                    btnStopPressed();
                    grabando = false;
                }
            }
        });

        fotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordVideo();
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
                RevisionFragment f = new RevisionFragment();
                Bundle args = new Bundle();
                args.putString("disciplina", disciplina);
                args.putSerializable("disciplinas", (Serializable) disciplinas);
                args.putString("especialidad", especialidad);
                args.putString("descripcion", descripcion);
                args.putString("complemento1", complemento1);
                args.putString("complemento2", complemento2);

                if (audios != null){
                    args.putSerializable("audios", (Serializable) audios);
                }else {
                    audios = null;
                }
                if (imagenes != null){
                    args.putSerializable("imagenes", (Serializable) imagenes);
                }else {
                    imagenes = null;
                }
                if (videos != null){
                    args.putSerializable("videos", (Serializable) videos);
                }else {
                    videos = null;
                }

                args.putSerializable("usuario", getArguments().getSerializable("usuario"));
                args.putSerializable("proyecto", getArguments().getSerializable("proyecto"));
                f.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container, f).addToBackStack("data").commit();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RESULT_OK && resultCode == RESULT_OK) {

        }
    }

    private void getMicrophonePermission(){
        if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions((Activity) context, new String[]
                    {Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
        }
    }

    private void btnRecordPressed(){
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setOutputFile(getRecordedFilePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void btnStopPressed(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private String getRecordedFilePath(){
        numAudioInc++;
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        //File file = new File(musicDirectory, etDescripcion.getText()+String.valueOf(numAudioInc)+".mp3");
        File file = new File(musicDirectory, "aaaa"+numAudioInc+".mp4");
        audios.add(file);
        return  file.getPath();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //imagenes.add(photoFile);
                Uri photoURI = FileProvider.getUriForFile(Objects.requireNonNull(context.getApplicationContext()),
                        context.getPackageName()+".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 102);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        imagenes.add(image);
        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    private void recordVideo(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(intent.resolveActivity(context.getPackageManager()) != null){
            if(intent.resolveActivity(context.getPackageManager()) != null){
                File videoFile = null;
                try {
                    videoFile = createVideoFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (videoFile != null){
                    //videos.add(videoFile);
                    Uri videoURI = FileProvider.getUriForFile(Objects.requireNonNull(context.getApplicationContext()),
                            context.getPackageName()+".provider",
                            videoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                    startActivityForResult(intent, RESULT_OK);
                }
            }
        }
    }


    private File createVideoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //String videoFileName = "MP4_" + timeStamp + "_";
        String videoFileName = "MP4_video_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File video = File.createTempFile(
                videoFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );
        videos.add(new File(video.getAbsolutePath()));
        // Save a file: path for use with ACTION_VIEW intents
        return video;
    }
}