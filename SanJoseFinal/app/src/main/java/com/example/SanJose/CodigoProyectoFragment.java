package com.example.SanJose;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.SanJose.Models.Proyecto;
import com.example.SanJose.Models.User;
import com.example.SanJose.databinding.FragmentCodigoProyectoBinding;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public class CodigoProyectoFragment extends Fragment {
    private Context context;

    private EditText etCodigoProyecto;

    private Button btnAnterior, btnSiguiente;

    private ImageView zoombtn, remoteEyeBtn;

    private Proyecto proyecto;

    private interface GetProyecto{
        @GET("getProyecto")
        Call<Proyecto> getProyecto(@Query("idProyecto") String idProyecto);
    }

    /*private interface GetProyecto{
        @GET("getProyecto_copy")
        Call<Proyecto> getProyecto(@Query("idProyecto") String idProyecto);
    }*/

    public CodigoProyectoFragment() {
        // Required empty public constructor
    }

    public static CodigoProyectoFragment newInstance() {
        CodigoProyectoFragment fragment = new CodigoProyectoFragment();
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
        return inflater.inflate(R.layout.fragment_codigo_proyecto, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etCodigoProyecto = getActivity().findViewById(R.id.etCodigoProyecto);
        btnAnterior = getActivity().findViewById(R.id.btnAnterior_CodigoP);
        btnSiguiente = getActivity().findViewById(R.id.btnSiguiente_CodigoP);
        zoombtn = getActivity().findViewById(R.id.zoom_CP);
        remoteEyeBtn = getActivity().findViewById(R.id.remoteeye_CP);

        zoombtn.setOnClickListener(new View.OnClickListener() {
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

        btnAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStackImmediate();
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCodigoProyecto.getText().toString().length() > 0) {
                    Retrofit retrofit = new Retrofit.Builder().baseUrl("URL A LA API")
                            .addConverterFactory(GsonConverterFactory.create()).build();
                    GetProyecto getProyecto = retrofit.create(GetProyecto.class);

                    Call<Proyecto> call = getProyecto.getProyecto(etCodigoProyecto.getText().toString());
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
                                Toast.makeText(context, "Proyecto no encontrado", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Proyecto> call, Throwable t) {
                            Toast.makeText(context, "Error de conexion, compruebe el wifi", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}