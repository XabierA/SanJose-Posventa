package com.example.SanJose;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.SanJose.Models.Proyecto;
import com.example.SanJose.databinding.FragmentQRBinding;
import com.google.zxing.Result;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class QRFragment extends Fragment {
    private Context context;

    private IControlFragmentos control;

    public CodeScanner mCodeScanner;

    public CodeScannerView scannerView;

    private Proyecto proyecto;


    private interface GetProyecto{
        @GET("getProyecto")
        Call<Proyecto> getProyecto(@Query("idProyecto") String idProyecto);
    }

    public static QRFragment newInstance() {return new QRFragment();}


    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    android.Manifest.permission.CAMERA}, 101);
        }

        View root = inflater.inflate(R.layout.fragment_q_r, container, false);
        scannerView = root.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setScanMode(ScanMode.CONTINUOUS);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkProyect(result.toString());
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCodeScanner.releaseResources();
    }

    public void checkProyect(String id){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("URL DE LA API")
                .addConverterFactory(GsonConverterFactory.create()).build();
        QRFragment.GetProyecto getProyecto = retrofit.create(QRFragment.GetProyecto.class);

        Call<Proyecto> call = getProyecto.getProyecto(id);
        call.enqueue(new Callback<Proyecto>() {
            @Override
            public void onResponse(Call<Proyecto> call, Response<Proyecto> response) {
                if (response.body() != null){
                    proyecto = response.body();
                    MainMenuFragment f = new MainMenuFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("usuario", getArguments().getSerializable("usuario"));
                    args.putSerializable("proyecto", (Serializable) proyecto);
                    f.setArguments(args);
                    getParentFragmentManager().beginTransaction().replace(R.id.nav_container,  f).addToBackStack("CP").commit();
                }else {
                    //Toast.makeText(context, "Proyecto no encontrado", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Proyecto> call, Throwable t) {
                Log.e("Incidencias", t.getMessage());
            }
        });
    }
}