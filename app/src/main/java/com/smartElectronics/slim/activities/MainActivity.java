 package com.smartElectronics.slim.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.smartElectronics.slim.network.model.DefaultResponse;
import com.smartElectronics.slim.R;
import com.smartElectronics.slim.network.RetrofitClient;
import com.smartElectronics.slim.storage.SharedPref;

import java.io.IOException;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // Global variable
    private TextView txtLogin;
    private MaterialButton btnSignUp;
    private EditText email,pass,name,school;
    private String TAG = "Main";


    @Override
    protected void onStart() {
        super.onStart();

        if(SharedPref.getInstance(getBaseContext()).isLoggedIn()) {
            Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

  
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.txt_login:
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                break;

            case R.id.btn_signUp:
                userSignUp();
                break;
        }
    }

    
    /// start methods
    
    private void initViews(){

        txtLogin = findViewById(R.id.txt_login);

        btnSignUp = findViewById(R.id.btn_signUp);

        email  = findViewById(R.id.edt_email);
        pass   = findViewById(R.id.edt_pass);
        name   = findViewById(R.id.edt_name);
        school = findViewById(R.id.edt_school);

        txtLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

    }

    
    private void userSignUp() {

        String email  = this.email.getText().toString().trim();
        String pass   = this.pass.getText().toString().trim();
        String name   = this.name.getText().toString().trim();
        String school = this.school.getText().toString().trim();
        String apiKey = UUID.randomUUID().toString();

        if(email.isEmpty()){
            this.email.setError("Email is required");
            this.email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            this.email.setError("Enter a valid email");
            this.email.requestFocus();
            return;
        }

        if(pass.isEmpty()){
            this.pass.setError("Password is required");
            this.pass.requestFocus();
            return;
        }
        if(pass.length() < 6){
            this.pass.setError("Password should be atleast 6 length");
            this.pass.requestFocus();
            return;
        }

        if(name.isEmpty()){
            this.name.setError("name is required");
            this.name.requestFocus();
            return;
        }
        if(school.isEmpty()){
            this.school.setError("school is required");
            this.school.requestFocus();
            return;
        }


        // Do user registration using the api call
        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(email, pass, name, school);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                if (response.code() == 201){

                    DefaultResponse result = response.body();
                    Toast.makeText(getBaseContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                    //SharedPref.getInstance(getBaseContext()).storeApiKey(apiKey);

                }else if (response.code() == 422) {
                    Toast.makeText(getBaseContext(), "User already Exist.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {


            }
        });
    }
}
