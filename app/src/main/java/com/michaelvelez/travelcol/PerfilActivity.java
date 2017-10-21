package com.michaelvelez.travelcol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static android.R.attr.resource;
import static com.michaelvelez.travelcol.R.id.image;

public class PerfilActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,NavigationView.OnNavigationItemSelectedListener {

    private String correoR,passwordR,correoG,nameG,urlG,nameF,urlF,correoF;
    private TextView tNombre,tPassword;
    private GoogleApiClient mGoogleApiClient;
    private int optlog;
    private ImageView imagen;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        tNombre = (TextView)findViewById(R.id.tNombre);
        tPassword = (TextView)findViewById(R.id.tPassword);

//        FrameLayout contentFrameLayout = (FrameLayout)findViewById(R.id.content);
//        getLayoutInflater().inflate(R.layout.activity_splash,contentFrameLayout);


        Bundle extras = getIntent().getExtras();
        correoR = extras.getString("correo");
        passwordR = extras.getString("password");
//        optlog = extras.getInt("optlog");
        nameG = extras.getString("nameG");
        correoG = extras.getString("correoG");
        urlG = extras.getString("urlG");
        nameF = extras.getString("nameF");
        urlF = extras.getString("urlF");
        correoF = extras.getString("correoF");



        imagen = (ImageView)findViewById(R.id.imagen) ;

        prefs = getSharedPreferences("SP" , Context.MODE_PRIVATE);
        editor = prefs.edit();
        final int optLog = prefs.getInt("optlog",0);

        if(optLog == 1){
            tNombre.setText("Usuario :" + "\n" + correoR);
            tPassword.setText("Contraseña :" + "\n" +passwordR);
            imagen.setImageResource(R.drawable.usuario);

//            Toast.makeText(getApplication(),"si entro",Toast.LENGTH_SHORT).show();


        }
        else if (optLog == 2){
//            Toast.makeText(getApplicationContext(),"lol",Toast.LENGTH_SHORT).show();
            tNombre.setText("Usuario :" + "\n" + nameG);
            tPassword.setText("Correo:" + "\n" + correoG);
            Glide.with(this).load(urlG).crossFade().placeholder(R.drawable.usuario).into(imagen);

        }
        else if (optLog == 3){
//            Toast.makeText(getApplicationContext(),nameF,Toast.LENGTH_SHORT).show();
            tNombre.setText("Usuario :" + "\n" + nameF);
            tPassword.setText("Correo" + "\n" + correoF);
            Glide.with(this).load(urlF).crossFade().placeholder(R.drawable.usuario).into(imagen);


        }




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

    }




    //    private void handleSignResult(GoogleSignInResult result) {
//
//        if (result.isSuccess()){
//            GoogleSignInAccount account = result.getSignInAccount();
//
//
//        }
//        else {Intent intent = new Intent(PerfilActivity.this,LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();}
//    }
    //LOg out google
      public void logOutG (){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()){
                    Intent intent = new Intent(PerfilActivity.this,LoginActivity.class);
                    intent.putExtra("correo",correoR);
                    intent.putExtra("password",passwordR);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }
                else {
                    Toast.makeText(getApplicationContext(),"No se pudo cerrar session",Toast.LENGTH_SHORT).show();}

            }
        });

    }

    //Logout fb

    public void logOutF(){

        LoginManager.getInstance().logOut();
        Intent intent = new Intent(PerfilActivity.this,LoginActivity.class);
        intent.putExtra("correo",correoR);
        intent.putExtra("password",passwordR);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mperfil,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        prefs = getSharedPreferences("SP" , Context.MODE_PRIVATE);
        editor = prefs.edit();
        final int optLog = prefs.getInt("optlog",0);

        switch (id){
            case R.id.mPrincipal:

                prefs = getSharedPreferences("SP", Context.MODE_PRIVATE);
                editor = prefs.edit();
                //almacenamos el valor de optLog
                editor.putInt("optlog",optLog);
                intent = new Intent(PerfilActivity.this,MainActivity.class);
                intent.putExtra("correo",correoR);
                intent.putExtra("password",passwordR);
                intent.putExtra("optlog",optlog);
                intent.putExtra("nameG",nameG);
                intent.putExtra("correoG",correoG);
                intent.putExtra("urlG",urlG);
                intent.putExtra("nameF",nameF);
                intent.putExtra("urlF",urlF);
                intent.putExtra("correoF",correoF);


                startActivity(intent);
                finish();
                break;
            case R.id.mCerrar:


                if(optLog == 1){
                    intent = new Intent(PerfilActivity.this,LoginActivity.class);
                    intent.putExtra("correo",correoR);
                    intent.putExtra("password",passwordR);
                    prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
                    editor = prefs.edit();
                    //almacenamos el valor de optLog
                    editor.putInt("optlog",0);
                    editor.commit();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    finish();

                }
                else if (optLog == 2 ){logOutG();
                    prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
                    editor = prefs.edit();
                    //almacenamos el valor de optLog
                    editor.putInt("optlog",0);
                    editor.commit();
                }
                else if (optLog == 3){logOutF();
                    prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
                    editor = prefs.edit();
                    //almacenamos el valor de optLog
                    editor.putInt("optlog",0);
                    editor.commit();
                }
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
