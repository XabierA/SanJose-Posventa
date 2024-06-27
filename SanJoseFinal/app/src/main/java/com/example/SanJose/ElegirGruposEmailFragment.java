package com.example.SanJose;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.SanJose.FragmentCallBack.FragmentCallBack;
import com.example.SanJose.MAILSENDER.GMailSender;
import com.example.SanJose.Models.Email;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class ElegirGruposEmailFragment extends Fragment {
    private Context context;

    private Button btnCarpinteros, btnElectricistas, btnEncofradores;

    private FragmentCallBack fragmentCallback;

    private List<Email> emails = new ArrayList<>();

    private interface GetEmails{
        @GET("GetEmails")
        Call<List<Email>> getEmails(@Query("especialidad") String especialidad);
    }


    public ElegirGruposEmailFragment() {
        // Required empty public constructor
    }

    public static ElegirGruposEmailFragment newInstance() {
        ElegirGruposEmailFragment fragment = new ElegirGruposEmailFragment();
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
        return inflater.inflate(R.layout.fragment_elegir_grupos_email, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnCarpinteros = getView().findViewById(R.id.btnCarpinteros);
        btnElectricistas = getView().findViewById(R.id.btnElectricistas);
        btnEncofradores = getView().findViewById(R.id.btnEncofradores);

        btnCarpinteros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://eu-west-2.aws.data.mongodb-api.com/app/apisanjose-iciuj/endpoint/")
                        .addConverterFactory(GsonConverterFactory.create()).build();

                ElegirGruposEmailFragment.GetEmails requestEmails = retrofit.create(ElegirGruposEmailFragment.GetEmails.class);
                Call<List<Email>> call = requestEmails.getEmails("carpintero");
                call.enqueue(new Callback<List<Email>>() {
                    @Override
                    public void onResponse(Call<List<Email>> call, Response<List<Email>> response) {
                        List<Email> apiResponse = response.body();
                        emails = apiResponse;
                        try {
                            Log.v("emails", emails.get(0).email);
                            GMailSender sender = new GMailSender("testemailsenderapp@gmail.com", "jmdpwgebudgzfiom", emails);
                            sender.execute();
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Email>> call, Throwable t) {
                    }
                });
                ///////////////////
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
        });
        btnElectricistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://eu-west-2.aws.data.mongodb-api.com/app/apisanjose-iciuj/endpoint/")
                        .addConverterFactory(GsonConverterFactory.create()).build();

                ElegirGruposEmailFragment.GetEmails requestEmails = retrofit.create(ElegirGruposEmailFragment.GetEmails.class);
                Call<List<Email>> call = requestEmails.getEmails("electricista");
                call.enqueue(new Callback<List<Email>>() {
                    @Override
                    public void onResponse(Call<List<Email>> call, Response<List<Email>> response) {
                        List<Email> apiResponse = response.body();
                        emails = apiResponse;
                        try {
                            Log.v("emails", emails.get(0).email);
                            GMailSender sender = new GMailSender("testemailsenderapp@gmail.com", "jmdpwgebudgzfiom", emails);
                            sender.execute();
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Email>> call, Throwable t) {
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
        });
        btnEncofradores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://eu-west-2.aws.data.mongodb-api.com/app/apisanjose-iciuj/endpoint/")
                        .addConverterFactory(GsonConverterFactory.create()).build();

                ElegirGruposEmailFragment.GetEmails requestEmails = retrofit.create(ElegirGruposEmailFragment.GetEmails.class);
                Call<List<Email>> call = requestEmails.getEmails("encofrador");
                call.enqueue(new Callback<List<Email>>() {
                    @Override
                    public void onResponse(Call<List<Email>> call, Response<List<Email>> response) {
                        List<Email> apiResponse = response.body();
                        emails = apiResponse;
                        try {
                            Log.v("emails", emails.get(0).email);
                            GMailSender sender = new GMailSender("testemailsenderapp@gmail.com", "jmdpwgebudgzfiom", emails);
                            sender.execute();
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Email>> call, Throwable t) {
                    }
                });
                FragmentManager fm = getParentFragmentManager();
                for (int i = fm.getBackStackEntryCount() - 1; i > 0; i--) {
                    if (!fm.getBackStackEntryAt(i).getName().equalsIgnoreCase("main")) {
                        fm.popBackStack();
                    }
                    else
                    {
                        Log.v("AAAA", "entra");
                        break;
                    }
                }
                getParentFragmentManager().popBackStackImmediate();
            }
        });
    }

    protected void sendEmail() {
        Log.i("Send email", "");

        String[] TO = {"xabier.angulo@bexreal.com"};
        String[] CC = {"testemailsenderapp@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Se a creado una nueva incidencia, accede al portal web para revisarla");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void setFragmentCallback(FragmentCallBack callback) {
        this.fragmentCallback = callback;
    }
}