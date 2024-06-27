package com.example.SanJose;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SanJose.Adapters.IncidenciasTableAdapter;
import com.example.SanJose.Models.Incidencia;
import com.example.SanJose.Models.User;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class Incidencias_List_Fragment extends Fragment {

    private Context context;

    private User usuario;

    private IncidenciasTableAdapter agAdapter;

    private RecyclerView recyclerView;

    private List<Incidencia> incidencias = new ArrayList<Incidencia>();

    private interface RequestPaciente{
        @GET("getIncidencias")
        Call<List<Incidencia>> getIncidencias();
    }

    public Incidencias_List_Fragment() {
        // Required empty public constructor
    }

    public static Incidencias_Fragment newInstance() {
        Incidencias_Fragment fragment = new Incidencias_Fragment();
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
        return inflater.inflate(R.layout.fragment_incidencias_, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments().getSerializable("usuario") != null){
            usuario = (User) getArguments().getSerializable("usuario");
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://eu-west-2.aws.data.mongodb-api.com/app/apisanjose-iciuj/endpoint/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestPaciente requestPaciente = retrofit.create(RequestPaciente.class);
        Call<List<Incidencia>> call = requestPaciente.getIncidencias();
        call.enqueue(new Callback<List<Incidencia>>() {
            @Override
            public void onResponse(Call<List<Incidencia>> call, Response<List<Incidencia>> response) {
                List<Incidencia> apiResponse = response.body();
                if (apiResponse.size() > 0){
                    incidencias = apiResponse;
                    recyclerView = getView().findViewById(R.id.inc_recycleView);
                    agAdapter = new IncidenciasTableAdapter(incidencias, usuario);
                    RecyclerView.LayoutManager lManager =  new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(lManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(agAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Incidencia>> call, Throwable t) {
                Log.e("Incidencias", t.getMessage());
                Toast.makeText(context, "no focuchiona", Toast.LENGTH_LONG).show();
            }
        });
    }
}