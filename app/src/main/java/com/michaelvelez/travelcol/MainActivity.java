package com.michaelvelez.travelcol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.michaelvelez.travelcol.fragments.HomeFragment;
import com.michaelvelez.travelcol.fragments.NotificationFragment;
import com.michaelvelez.travelcol.fragments.TabFragment;
import com.michaelvelez.travelcol.fragments.ToursFragment;
import com.michaelvelez.travelcol.fragments.ValorarFragment;

import static com.google.android.gms.common.stats.zzb.zza.FM;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,NavigationView.OnNavigationItemSelectedListener {

    private String correoR,passwordR,correoG,nameG,urlG,nameF,urlF,correoF;
    private GoogleApiClient mGoogleApiClient;
    private int optlog;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    DrawerLayout drawer;

    private TextView mTextMessage;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ft.replace(R.id.content, new HomeFragment()).commit();
                    return true;
                case R.id.navigation_tours:
                    ft.replace(R.id.content, new ToursFragment()).commit();
                    return true;

            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, new HomeFragment()).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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

        View hView =  navigationView.getHeaderView(0);

        TextView tNameP = (TextView)hView.findViewById(R.id.tNameP);
        TextView tPasswordP = (TextView)hView.findViewById(R.id.tPasswordP);
        ImageView iPerfil = (ImageView)hView.findViewById(R.id.iPerfil) ;





        prefs = getSharedPreferences("SP" , Context.MODE_PRIVATE);
        editor = prefs.edit();
        final int optLog = prefs.getInt("optlog",0);

        if(optLog == 1){
            tNameP.setText("Usuario :" + "\n" + correoR);
            tPasswordP.setText("Contraseña :" + "\n" +passwordR);
            iPerfil.setImageResource(R.drawable.usuario);

//            Toast.makeText(getApplication(),"si entro",Toast.LENGTH_SHORT).show();


        }
        else if (optLog == 2){
//            Toast.makeText(getApplicationContext(),"lol",Toast.LENGTH_SHORT).show();
            tNameP.setText("Usuario :" + "\n" + nameG);
            tPasswordP.setText("Correo:" + "\n" + correoG);
            Glide.with(this).load(urlG).crossFade().placeholder(R.drawable.usuario).into(iPerfil);

        }
        else if (optLog == 3){
//            Toast.makeText(getApplicationContext(),nameF,Toast.LENGTH_SHORT).show();
            tNameP.setText("Usuario :" + "\n" + nameF);
            tPasswordP.setText("Correo" + "\n" + correoF);
            Glide.with(this).load(urlF).crossFade().placeholder(R.drawable.usuario).into(iPerfil);


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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
////        getMenuInflater().inflate(R.menu.main, menu);
////        return true;
//
//        getMenuInflater().inflate(R.menu.menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
////        int id = item.getItemId();
////
////        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            return true;
////        }
////
////        return super.onOptionsItemSelected(item);
//
//        int id = item.getItemId();
//        Intent intent;
//
//        prefs = getSharedPreferences("SP" , Context.MODE_PRIVATE);
//        editor = prefs.edit();
//        final int optLog = prefs.getInt("optlog",0);
//
//        switch (id){
//            case R.id.mPerfil:
//                prefs = getSharedPreferences("SP", Context.MODE_PRIVATE);
//                editor = prefs.edit();
//                //almacenamos el valor de optLog
//                editor.putInt("optlog",optLog);
//
//
//                intent = new Intent(MainActivity.this,PerfilActivity.class);
//                intent.putExtra("correo",correoR);
//                intent.putExtra("password",passwordR);
//
//                intent.putExtra("nameG",nameG);
//                intent.putExtra("correoG",correoG);
//                intent.putExtra("urlG",urlG);
//                intent.putExtra("nameF",nameF);
//                intent.putExtra("urlF",urlF);
//                intent.putExtra("correoF",correoF);
//
//
//                startActivity(intent);
//
//                break;
//            case R.id.mCerrar:
//
//
//
//                if(optLog == 1){
//                    intent = new Intent(MainActivity.this,LoginActivity.class);
//                    prefs = getSharedPreferences("SP", Context.MODE_PRIVATE);
//                    editor = prefs.edit();
//                    //almacenamos el valor de optLog
//                    editor.putInt("optlog",0);
//                    editor.commit();
//                    intent.putExtra("correo",correoR);
//                    intent.putExtra("password",passwordR);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                    startActivity(intent);
//                    finish();
//
//                }
//                else if (optLog == 2 ){logOutG();
//                    prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
//                    editor = prefs.edit();
//                    //almacenamos el valor de optLog
//                    editor.putInt("optlog",0);
//                    editor.commit();
//                }
//                else if (optLog == 3){logOutF();
//                    prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
//                    editor = prefs.edit();
//                    //almacenamos el valor de optLog
//                    editor.putInt("optlog",0);
//                    editor.commit();
//                }
////
////
//
//
//
//
//
//
//
//
//
//                break;
//            default:
//                break;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;

        prefs = getSharedPreferences("SP" , Context.MODE_PRIVATE);
        editor = prefs.edit();
        final int optLog = prefs.getInt("optlog",0);

        if (id == R.id.nav_principal) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content, new HomeFragment()).commit();
            // Handle the camera action
        } else if (id == R.id.nav_tours) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content, new ToursFragment()).commit();





        } else if (id == R.id.nav_experiencia) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content, new ValorarFragment()).commit();


        } else if (id == R.id.nav_tabs) {


            FragmentManager fm;
            FragmentTransaction ft;



            fm= getSupportFragmentManager();
            ft= fm.beginTransaction();
            ft.replace(R.id.content, new TabFragment()).commit();






        } else if (id == R.id.nav_cc) {

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

        } else if (id == R.id.nav_about) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content, new NotificationFragment()).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        Intent intent;
//
//        prefs = getSharedPreferences("SP" , Context.MODE_PRIVATE);
//        editor = prefs.edit();
//        final int optLog = prefs.getInt("optlog",0);
//
//        switch (id){
//            case R.id.mPerfil:
//                prefs = getSharedPreferences("SP", Context.MODE_PRIVATE);
//                editor = prefs.edit();
//                //almacenamos el valor de optLog
//                editor.putInt("optlog",optLog);
//
//
//                intent = new Intent(MainActivity.this,PerfilActivity.class);
//                intent.putExtra("correo",correoR);
//                intent.putExtra("password",passwordR);
//
//                intent.putExtra("nameG",nameG);
//                intent.putExtra("correoG",correoG);
//                intent.putExtra("urlG",urlG);
//                intent.putExtra("nameF",nameF);
//                intent.putExtra("urlF",urlF);
//                intent.putExtra("correoF",correoF);
//
//
//                startActivity(intent);
//
//                break;
//            case R.id.mCerrar:
//
//
//
//                if(optLog == 1){
//                   intent = new Intent(MainActivity.this,LoginActivity.class);
//                    prefs = getSharedPreferences("SP", Context.MODE_PRIVATE);
//                    editor = prefs.edit();
//                    //almacenamos el valor de optLog
//                    editor.putInt("optlog",0);
//                    editor.commit();
//                    intent.putExtra("correo",correoR);
//                    intent.putExtra("password",passwordR);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                   startActivity(intent);
//                   finish();
//
//                }
//                else if (optLog == 2 ){logOutG();
//                    prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
//                    editor = prefs.edit();
//                    //almacenamos el valor de optLog
//                    editor.putInt("optlog",0);
//                    editor.commit();
//                }
//                else if (optLog == 3){logOutF();
//                    prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
//                    editor = prefs.edit();
//                    //almacenamos el valor de optLog
//                    editor.putInt("optlog",0);
//                    editor.commit();
//                }
////
////
//
//
//
//
//
//
//
//
//
//                break;
//            default:
//                break;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
