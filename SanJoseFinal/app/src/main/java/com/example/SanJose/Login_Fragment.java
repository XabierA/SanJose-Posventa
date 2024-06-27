package com.example.SanJose;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

import com.example.SanJose.Models.Incidencia;
import com.example.SanJose.Models.User;
import com.example.SanJose.databinding.FragmentLoginBinding;

import java.io.Serializable;
import java.time.LocalDateTime;

import at.favre.lib.crypto.bcrypt.BCrypt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;


public class Login_Fragment extends Fragment {
    private Context context;

    private EditText etUser, etPassword;

    private ImageView zoomBtn, remoteEyeBtn;

    private Button loginBtn;

    private User usuario;

    private interface PostRequestLogin{
        @POST("login")
        Call<User> logUser(@Body User user);
    }

    /*private interface PostRequestLogin{
        @POST("login_testing")
        Call<User> logUser(@Body User user);
    }*/

    public Login_Fragment() {
        // Required empty public constructor
    }

    public static Login_Fragment newInstance() {
        Login_Fragment fragment = new Login_Fragment();
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
        return inflater.inflate(R.layout.fragment_login_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etUser = getActivity().findViewById(R.id.etUsername);
        etPassword = getActivity().findViewById(R.id.etPassword);
        loginBtn = getActivity().findViewById(R.id.loginBtn);
        zoomBtn = getActivity().findViewById(R.id.zoom_login);
        remoteEyeBtn = getActivity().findViewById(R.id.remoteeye_login);

        LocalDateTime fecha = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            fecha = LocalDateTime.now();
        }


        Retrofit retrofit = new Retrofit.Builder().baseUrl("URL DE LA API")
                .addConverterFactory(GsonConverterFactory.create()).build();


        etUser.clearFocus();
        etPassword.clearFocus();

        zoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("us.zoom.videomeetings");//com.example.soporte
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

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUser.getText().toString();
                String password = etPassword.getText().toString();

                String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), bcryptHashString);

                User user = new User(username);
                if(etUser.length() > 0 && etPassword.length() > 0){
                    Login_Fragment.PostRequestLogin postLogin = retrofit.create(Login_Fragment.PostRequestLogin.class);
                    Call<User> call = postLogin.logUser(user);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            usuario = response.body();
                            if(usuario != null){
                                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), usuario.password);
                                if(result.verified){
                                    ProyectoFragment f = new ProyectoFragment();
                                    Bundle args = new Bundle();
                                    args.putSerializable("usuario", (Serializable) usuario);
                                    f.setArguments(args);
                                    getParentFragmentManager().beginTransaction().replace(R.id.nav_container, f).addToBackStack("login").commit();
                                }else {
                                    usuario = null;
                                    Toast.makeText(context, "usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(context, "usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(context, "Error de conexion, compruebe el wifi", Toast.LENGTH_LONG).show();
                        }
                    });
                }else {
                    Toast.makeText(context, "Faltan parametros", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}