package com.michaelvelez.travelcol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private String correoR,passwordR,correoG,nameG,urlG,nameF,urlF,correoF;
    private GoogleApiClient mGoogleApiClient;
    private int optlog;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Bundle extras = getIntent().getExtras();
//        correoR = extras.getString("correo");
//        passwordR = extras.getString("password");

        Bundle extras = this.getIntent().getExtras();
        if(extras !=null){
             correoR = extras.getString("correo");
             passwordR = extras.getString("password");
            optlog = extras.getInt("optlog");
            nameG = extras.getString("nameG");
            correoG = extras.getString("correoG");
            urlG = extras.getString("urlG");
            nameF = extras.getString("nameF");
            urlF = extras.getString("urlF");
            correoF = extras.getString("correoF");










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

 //Google

//    @Override
//
//    protected void onStart() {
//        super.onStart();
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()){
//            GoogleSignInResult result = opr.get();
//            handleSignResult(result);
//
//
//        }
//
//        else {
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
//                    handleSignResult(googleSignInResult);
//                    Toast.makeText(getApplicationContext(),"Fail weon",Toast.LENGTH_SHORT).show();
//
//
//                }
//            });
//        }
//    }

//    private void handleSignResult(GoogleSignInResult result) {
//
//        if (result.isSuccess()){
//            GoogleSignInAccount account = result.getSignInAccount();
//
//        }
//        else {Intent intent = new Intent(MainActivity.this,LoginActivity.class);
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
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
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
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        intent.putExtra("correo",correoR);
        intent.putExtra("password",passwordR);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
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
            case R.id.mPerfil:
                prefs = getSharedPreferences("SP", Context.MODE_PRIVATE);
                editor = prefs.edit();
                //almacenamos el valor de optLog
                editor.putInt("optlog",optLog);


                intent = new Intent(MainActivity.this,PerfilActivity.class);
                intent.putExtra("correo",correoR);
                intent.putExtra("password",passwordR);

                intent.putExtra("nameG",nameG);
                intent.putExtra("correoG",correoG);
                intent.putExtra("urlG",urlG);
                intent.putExtra("nameF",nameF);
                intent.putExtra("urlF",urlF);
                intent.putExtra("correoF",correoF);


                startActivity(intent);

                break;
            case R.id.mCerrar:



                if(optLog == 1){
                   intent = new Intent(MainActivity.this,LoginActivity.class);
                    prefs = getSharedPreferences("SP", Context.MODE_PRIVATE);
                    editor = prefs.edit();
                    //almacenamos el valor de optLog
                    editor.putInt("optlog",0);
                    editor.commit();
                    intent.putExtra("correo",correoR);
                    intent.putExtra("password",passwordR);
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
//
//









                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
