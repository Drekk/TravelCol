package com.michaelvelez.travelcol;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private String correoR,passwordR,correoC,passwordC,correoG,nameG,urlG,nameF,correoF,urlF;
    private EditText eCorreo,ePassword;
    private Button bIniciar;
    private TextView tRegistrar;
    private GoogleApiClient mGoogleApiClient;

    private int RC_SIGN_IN=5678;
    private SignInButton signInButton;
    private int optLog;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eCorreo = (EditText)findViewById(R.id.eCorreo);
        ePassword = (EditText)findViewById(R.id.ePassword);
        bIniciar = (Button) findViewById(R.id.bIniciar);
        tRegistrar = (TextView)findViewById(R.id.tRegistrar);
        signInButton = (SignInButton)findViewById(R.id.sign_in_button) ;
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));

        Bundle extras = this.getIntent().getExtras();
        if(extras !=null){
            correoR = extras.getString("correo");
            passwordR = extras.getString("password");


        }



        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.michaelvelez.travelcol",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("lel:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    //facebook

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {



                System.out.println("onSuccess");
                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Procesando datos...");
                progressDialog.show();
                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        //Obtener datos de fb login
                        Bundle bFacebookData = getFacebookData(object);



                        correoF = bFacebookData.getString("email");
                        urlF = bFacebookData.getString("profile_pic");
                        nameF = bFacebookData.getString("first_name") + " " +  bFacebookData.getString("last_name") ;

                        Log.v("lograremos", nameF);
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
                        editor = prefs.edit();
                        //almacenamos el valor de optLog
                        editor.putInt("optlog",3);
                        editor.commit();
//                            intent.putExtra("optlog",optLog);
                            intent.putExtra("correo",correoR);
                            intent.putExtra("password",passwordR);
                            intent.putExtra("correoF",correoF);
                            intent.putExtra("urlF",urlF);
                            intent.putExtra("nameF",nameF);

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();







//                if(Profile.getCurrentProfile() == null) {
//                    mProfileTracker = new ProfileTracker() {
//                        @Override
//                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
//                            Log.v("cebook - profile", currentProfile.getName());
//                            nameF = currentProfile.getName();
//                            urlF = currentProfile.getProfilePictureUri(200 ,200).toString();
//
//                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                            optLog = 3;
//                            intent.putExtra("optlog",optLog);
//                            intent.putExtra("nameF",nameF);
//                            intent.putExtra("urlF",urlF);
//
//
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            finish();
//
//                            mProfileTracker.stopTracking();
//                        }
//                    };
//                    // no need to call startTracking() on mProfileTracker
//                    // because it is called by its constructor, internally.
//                }
//                else {
//                    Profile profile = Profile.getCurrentProfile();
//                    Log.v("facebook - profile", profile.getFirstName());
//                    nameF = profile.getName();
//                    urlF = profile.getProfilePictureUri(200 ,200).toString();
//                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                    optLog = 3;
//                    intent.putExtra("optlog",optLog);
//                    intent.putExtra("nameF",nameF);
//                    intent.putExtra("urlF",urlF);
//
//
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();
//                }







            }

            @Override
            public void onCancel() {

                Toast.makeText(getApplicationContext(),"Login Cancelado",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"Ocurrio un error al ingresar!",Toast.LENGTH_SHORT).show();

            }
        });



//        cargar();
//Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this/* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setColorScheme(signInButton.COLOR_DARK);

       signInButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
               startActivityForResult(signInIntent, RC_SIGN_IN);
           }
       });
//*Google

    }

    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=200");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        }
        catch(JSONException e) {
            Log.d("logrado","Error parsing JSON");
        }
        return null;
    }



//
//    public void cargar(){
//        SharedPreferences cargar= getSharedPreferences("preferencias",Context.MODE_PRIVATE);
//
//        eCorreo.setText(cargar.getString("email",""));
//        ePassword.setText(cargar.getString("pass",""));
//        correoR = eCorreo.getText().toString();
//        passwordR=ePassword.getText().toString();
//        eCorreo.setText("");
//        ePassword.setText("");
//
//    }



//    public void guardar(){
//        SharedPreferences cargar= getSharedPreferences("preferencias",Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = cargar.edit();
//        cp = ePassword.getText().toString();
//        cc = eCorreo.getText().toString();
//        editor.putString("email",cc);
//        editor.putString("pass",cp);
//        correoR = cc;passwordR=cp;
//        editor.commit();
//
//
//    }

    public void iniciar (View view){

        passwordC = ePassword.getText().toString();
        correoC = eCorreo.getText().toString();


        if (correoC.equals(correoR) && passwordC.equals(passwordR)){
//            guardar();

         prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
         editor = prefs.edit();
         //almacenamos el valor de optLog
         editor.putInt("optlog",1);
         editor.commit();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("correo",correoR);
        intent.putExtra("password",passwordR);


        startActivity(intent);
        finish();}

        else{ eCorreo.setText("");
        ePassword.setText("");
        eCorreo.setError("Usuario o contraseña incorrecta!");
        eCorreo.requestFocus();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1234 && resultCode == RESULT_OK){

            correoR = data.getExtras().getString("correo");
            passwordR = data.getExtras().getString("password");
            eCorreo.setText("");
            ePassword.setText("");
            eCorreo.setError("Digite los datos!");

           // Toast.makeText(this,correoR,Toast.LENGTH_SHORT).show();
            Log.d("correo",correoR);
            Log.d("password",passwordR);


        }

        else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);


        }

        else {callbackManager.onActivityResult(requestCode,resultCode,data);}

}

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("google", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
//            GoogleSignInAccount acct = result.getSignInAccount();

            GoogleSignInAccount account = result.getSignInAccount();
            Log.d("drekk",account.getPhotoUrl().toString());
            nameG = account.getDisplayName();
            correoG = account.getEmail();
            urlG = account.getPhotoUrl().toString();

            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
            editor = prefs.edit();
            //almacenamos el valor de optLog
            editor.putInt("optlog",2);
            editor.commit();
//            optLog = 2;
//            intent.putExtra("optlog",optLog);
            intent.putExtra("correo",correoR);
            intent.putExtra("password",passwordR);
            intent.putExtra("nameG",nameG);
            intent.putExtra("correoG",correoG);
            intent.putExtra("urlG",urlG);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getApplication(),"Error en login",Toast.LENGTH_SHORT).show();
        }
    }

    public void registrar(View view) {

        Intent intent = new Intent(LoginActivity.this,RegistroActivity.class);


        startActivityForResult(intent,1234);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplication(),"error en el login",Toast.LENGTH_SHORT).show();
    }
}
