package com.example.SanJose;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.SanJose.FragmentCallBack.FragmentCallBack;

import java.io.File;
import java.io.IOException;

public class Locate_Fragment extends Fragment {
    private Context context;

    private FragmentCallBack fragmentCallback;

    private ImageView plano, grid;

    private ImageButton gridBtn, eyeBtn;

    private Button audioBtn, guardarBtn;

    private boolean grabando = false;

    private MediaRecorder mediaRecorder;

    private File audio;

    public Locate_Fragment() {
        // Required empty public constructor
    }

    public static Locate_Fragment newInstance() {
        Locate_Fragment fragment = new Locate_Fragment();
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
        return inflater.inflate(R.layout.fragment_locate_, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        plano = getView().findViewById(R.id.plano);
        grid = getView().findViewById(R.id.grid);
        gridBtn = getView().findViewById(R.id.btnGrid);
        eyeBtn = getView().findViewById(R.id.btnEye);
        audioBtn = getView().findViewById(R.id.btnSeñalarPlano);
        guardarBtn = getView().findViewById(R.id.btnGuardar);


        grid.setImageBitmap(getBitmapFromResources(getResources(), R.drawable.grid_final));
        plano.setImageBitmap(getBitmapFromResources(getResources(), R.drawable.planofinal));

        gridBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (grid.getVisibility() == View.VISIBLE){
                    grid.setVisibility(View.GONE);
                }else {
                    grid.setVisibility(View.VISIBLE);
                }
            }
        });

        eyeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gridBtn.getVisibility() == View.VISIBLE){
                    gridBtn.setVisibility(View.GONE);
                    audioBtn.setVisibility(View.GONE);
                    guardarBtn.setVisibility(View.GONE);
                }else {
                    gridBtn.setVisibility(View.VISIBLE);
                    audioBtn.setVisibility(View.VISIBLE);
                    guardarBtn.setVisibility(View.VISIBLE);
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

        guardarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentCallback != null){
                    fragmentCallback.onDataSent(audio);
                }
                getParentFragmentManager().popBackStackImmediate();
            }
        });
    }

    public static Bitmap getBitmapFromResources(Resources resources, int resImage) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inSampleSize = 1;
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        return BitmapFactory.decodeResource(resources, resImage, options);
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
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        //File file = new File(musicDirectory, etDescripcion.getText()+String.valueOf(numAudioInc)+".mp3");
        File file = new File(musicDirectory, "locateaudio.mp3");
        audio = file;
        return  file.getPath();
    }

    public void setFragmentCallback(FragmentCallBack callback) {
        this.fragmentCallback = callback;
    }
}