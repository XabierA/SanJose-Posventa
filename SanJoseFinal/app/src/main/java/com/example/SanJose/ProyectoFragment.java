package com.example.SanJose;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.SanJose.Models.User;
import com.example.SanJose.databinding.FragmentProyectoBinding;

import java.io.Serializable;

import at.favre.lib.crypto.bcrypt.BCrypt;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class ProyectoFragment extends Fragment {
    private Context context;

    private Button codigoBtn;

    private ImageView zoomBtn, remoteEyeBtn;

    private ImageButton QRBtn;

    public ProyectoFragment() {
        // Required empty public constructor
    }

    public static ProyectoFragment newInstance() {
        ProyectoFragment fragment = new ProyectoFragment();
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
        return inflater.inflate(R.layout.fragment_proyecto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        QRBtn = getActivity().findViewById(R.id.btnQR);
        codigoBtn = getActivity().findViewById(R.id.btnCodigo);
        zoomBtn = getActivity().findViewById(R.id.zoom_Proyecto);
        remoteEyeBtn = getActivity().findViewById(R.id.remoteeye_proyecto);

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

        QRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRFragment f = new QRFragment();
                Bundle args = new Bundle();
                args.putSerializable("usuario", getArguments().getSerializable("usuario"));
                f.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container, f).addToBackStack("proyecto").commit();
            }
        });

        codigoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CodigoProyectoFragment f = new CodigoProyectoFragment();
                Bundle args = new Bundle();
                args.putSerializable("usuario", getArguments().getSerializable("usuario"));
                f.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_container, f).addToBackStack("proyecto").commit();
            }
        });
    }
}