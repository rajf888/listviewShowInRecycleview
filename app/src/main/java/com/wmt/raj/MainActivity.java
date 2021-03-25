package com.wmt.raj;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.wmt.raj.constant.Constant;
import com.wmt.raj.modal.Modal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {

    OkHttpHandler okHttpHandler;
    EditText editUser,editEmail,editPass,editConfirmPass;
    String username,pass,email,confirmPass;
    Button submit;
    Boolean isEmailValid = false;
    Boolean isPasswordValid = false;
    Boolean isConfirmPasswordValid = false;
    Boolean isUserValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editUser = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        editPass = findViewById(R.id.editPass);
        editConfirmPass = findViewById(R.id.editConfirmPass);
        submit = findViewById(R.id.btnSignUp);
//        Intent intent = new Intent(getApplicationContext(), ListActivity.class);
//        intent.putExtra("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjEzNzksImlhdCI6MTYxNjYwNjA5OH0.3kktuGh1dtp5cyalmEmwl9wCn2a7Cwh3RpIVpPrd-5k");
//        startActivity(intent);


       submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(setValidation(editUser,editEmail,editPass,editConfirmPass)){
                   Log.v(">>> ","ALL DATA ARE VALID");
                   username = editUser.getText().toString().trim();
                   email = editEmail.getText().toString().trim();
                   pass = editPass.getText().toString().trim();
                   confirmPass = editConfirmPass.getText().toString().trim();
                   okHttpHandler = new OkHttpHandler();
                   okHttpHandler.execute(username,email,pass);
               }else{
                   Log.v(">>> ","ALL DATA ARE NOT VALID");
               }
           }
       });

    }

    private final class OkHttpHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormEncodingBuilder()
                    .add("username", params[0])
                    .add("email", params[1])
                    .add("password", params[2])
                    .build();
            Request.Builder builder = new Request.Builder();

            builder.url(Constant.URL_LOGIN).post(formBody);
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            Log.v(">>> ","LOGIN RESPONSE : "+result);
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject meta = jsonObj.getJSONObject("meta");
                if(meta.getString("status").equalsIgnoreCase("ok")){
                    JSONObject data = jsonObj.getJSONObject("data");
                    JSONObject token = data.getJSONObject("token");
                    getApplicationContext().getSharedPreferences("tokenStore", MODE_PRIVATE).edit().putString("token", token.toString()).apply();

                    Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                    intent.putExtra("token", token.getString("token"));
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "FAILED : "+meta.getString("message"), Toast.LENGTH_SHORT);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean setValidation(EditText user,EditText email,EditText password,EditText confirmPass) {

        if(!user.getText().toString().matches("[a-zA-Z0-9]+")){
            user.setError("Username must be alphanumeric");
            isUserValid = false;
        }
        else if(!user.getText().toString().matches("^.*[0-9].*$")){
            user.setError("it should contain numbers");
            isUserValid = false;
        }
        else if(!user.getText().toString().matches("^.*[a-zA-Z].*$")){
            user.setError("it should contain letters");
            isUserValid = false;
        }else {
            isUserValid = true;
        }


        if (email.getText().toString().isEmpty()) {
            email.setError("Please enter email.");
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Please enter valid email.");
            isEmailValid = false;
        } else  {
            isEmailValid = true;
        }


        if (password.getText().toString().isEmpty()) {
            password.setError("Please Enter Password.");
            isPasswordValid = false;
        } else if (password.getText().length() < 3) {
            password.setError("Password is too short.");
            isPasswordValid = false;
        } else  {
            isPasswordValid = true;
        }

        if (confirmPass.getText().toString().isEmpty()) {
            confirmPass.setError("Please Enter Password.");
            isConfirmPasswordValid = false;
        } else if (confirmPass.getText().length() < 3) {
            confirmPass.setError("Password is too short.");
            isConfirmPasswordValid = false;
        } else  if(!password.getText().toString().trim().equals(confirmPass.getText().toString().trim())){
            confirmPass.setError("Password is not match with above please check.");
            isConfirmPasswordValid = false;
        }else  {
            isConfirmPasswordValid = true;
        }


        if(isUserValid && isEmailValid && isPasswordValid && isConfirmPasswordValid){
            return true;
        }else{
            return false;
        }
    }

}