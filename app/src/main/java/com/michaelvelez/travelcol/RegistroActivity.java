package com.michaelvelez.travelcol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {


    private String correo,password,rpassword;
    private EditText eCorreo,ePassword,eRpassword;
    private Button bRegistrar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        eCorreo = (EditText) findViewById(R.id.eCorreo);
        ePassword = (EditText) findViewById(R.id.ePassword);
        eRpassword = (EditText) findViewById(R.id.eRpassword);



    }

   public static boolean isEmailValid(String email) {
        boolean isValid;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;


        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;


        }else{isValid = false;


        }
        return isValid;

   }

    public void registrar(View view){

        correo = eCorreo.getText().toString();
        password = ePassword.getText().toString();
        rpassword = eRpassword.getText().toString();





        if (!TextUtils.isEmpty(correo) && !TextUtils.isEmpty(password)&& !TextUtils.isEmpty(rpassword) ) {

            if(isEmailValid(correo)==true){
                if (password.equals(rpassword)) {


                    Intent intent = new Intent();
                    intent.putExtra("correo", correo);
                    intent.putExtra("password", password);
                    setResult(RESULT_OK, intent);
                    finish();

                } else {

                    ePassword.setText("");
                    eRpassword.setText("");
                    ePassword.setError("Digite nuevamente,las contraseñas no cocinciden!");
                    ePassword.requestFocus();}
            }

            else {eCorreo.setError("Formato e-mail invalido");
            eCorreo.requestFocus();
            }



        }

        else {if(TextUtils.isEmpty(correo)){eCorreo.setError("Digite el dato ");eCorreo.requestFocus();}
            if(TextUtils.isEmpty(password)){ePassword.setError("Digite el dato ");ePassword.requestFocus();}
            if(TextUtils.isEmpty(rpassword)){eRpassword.setError("Digite el dato ");eRpassword.requestFocus();}}


    }
}
