package com.example.SanJose.ViewModels;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SanJose.Adapters.GaleriaAdapter;
import com.example.SanJose.R;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Image_Gallery_Fragment extends Fragment {

    // InteriorViewModel mViewModel;
    private GaleriaAdapter gAdapter;
    private RecyclerView grid;

    private List<Serializable> galeria = new ArrayList<Serializable>();
    private Context context;

    private Button anteriorBtn;

    public static Image_Gallery_Fragment newInstance() {
        return new Image_Gallery_Fragment();
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inc__image__gallery_, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        anteriorBtn = getView().findViewById(R.id.btnAnterior_Galeria);
        //mViewModel = new ViewModelProvider(this).get(InteriorViewModel.class);

        if (getArguments().getSerializable("imagenes") == null){
            galeria = (List<Serializable>) getArguments().getSerializable("videos");
            grid=getView().findViewById(R.id.recycledGaleria);

            gAdapter = new GaleriaAdapter(galeria,getParentFragmentManager(), "videos");
        }else {
            galeria = (List<Serializable>) getArguments().getSerializable("imagenes");
            grid=getView().findViewById(R.id.recycledGaleria);

            gAdapter = new GaleriaAdapter(galeria,getParentFragmentManager(), "imagenes");
        }

        RecyclerView.LayoutManager lManager =  new GridLayoutManager(context, 3);
        grid.setLayoutManager(lManager);
        grid.setItemAnimator(new DefaultItemAnimator());
        grid.setAdapter(gAdapter);
        anteriorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStackImmediate();
            }
        });
    }

}