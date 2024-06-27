package com.example.SanJose;

import static android.app.Activity.RESULT_OK;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.SanJose.databinding.FragmentDescDocBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Desc_Doc_Fragment extends Fragment {

    private static int MICROPHONE_PERMISSION_CODE = 200;
    private Context context;

    MediaRecorder mediaRecorder;

    MediaPlayer mediaPlayer;

    private Button audioBtn, videoBtn, fotoBtn, anteriorBtn, siguienteBtn;

    private EditText etDescripcion;

    private List<String> audios = new ArrayList<String>();

    private List<File> imagenes = new ArrayList<File>();

    private boolean grabando = false;

    private int numAudioInc = 0;
    private String currAudio;

    private String currentPhotoPath;

    private Uri recordedVideo;

    public Desc_Doc_Fragment() {
        // Required empty public constructor
    }

    public static Desc_Doc_Fragment newInstance() {
        Desc_Doc_Fragment fragment = new Desc_Doc_Fragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_desc__doc_, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        audioBtn = getView().findViewById(R.id.audioBtn);
        videoBtn = getView().findViewById(R.id.videoBtn);
        fotoBtn = getView().findViewById(R.id.fotoBtn);
        etDescripcion = getView().findViewById(R.id.etDescripcion);
        anteriorBtn = getView().findViewById(R.id.btnAnterior_Docs);
        siguienteBtn = getView().findViewById(R.id.btnSiguiente_Docs);

        getMicrophonePermission();

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA}, 101);
        }

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
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container, Plano_Fragment.newInstance()).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RESULT_OK && resultCode == RESULT_OK) {
            recordedVideo = intent.getData();
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
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(getRecordedFilePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(context, "Grabacion iniciada", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void btnStopPressed(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        audios.add(currAudio);
        Toast.makeText(context, "Grabacion detenida", Toast.LENGTH_LONG).show();
    }

    private String getRecordedFilePath(){
        numAudioInc++;
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, etDescripcion.getText()+String.valueOf(numAudioInc)+".mp3");
        currAudio = file.getPath();
        return  file.getPath();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                imagenes.add(photoFile);
                Uri photoURI = FileProvider.getUriForFile(Objects.requireNonNull(context.getApplicationContext()),
                        context.getPackageName()+".provider",
                        photoFile);
                //Toast.makeText(context, photoURI+"", Toast.LENGTH_LONG).show();
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
        Toast.makeText(context, storageDir+"", Toast.LENGTH_LONG).show();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void recordVideo(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(intent.resolveActivity(context.getPackageManager()) != null){
            startActivityForResult(intent, RESULT_OK);
        }
    }
}