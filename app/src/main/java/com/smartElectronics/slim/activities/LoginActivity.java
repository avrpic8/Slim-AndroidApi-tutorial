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
import com.smartElectronics.slim.R;
import com.smartElectronics.slim.network.RetrofitClient;
import com.smartElectronics.slim.network.model.LoginResponse;
import com.smartElectronics.slim.storage.SharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    EditText edtEmail, edtPass;
    TextView txtRegister;
    MaterialButton btnLogin;
    private String TAG = "login";

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
        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews(){

        edtEmail = findViewById(R.id.edt_email);
        edtPass  = findViewById(R.id.edt_pass);

        txtRegister = findViewById(R.id.txt_register);
        btnLogin = findViewById(R.id.btn_login);

        txtRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.txt_register:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;

            case R.id.btn_login:
                userLogin();
                break;
        }
    }

    private void userLogin() {

        String email = edtEmail.getText().toString().trim();
        String pass  = edtPass.getText().toString().trim();

        if(email.isEmpty()){
            this.edtEmail.setError("Email is required");
            this.edtEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            this.edtEmail.setError("Enter a valid email");
            this.edtEmail.requestFocus();
            return;
        }

        if(pass.isEmpty()){
            this.edtPass.setError("Password is required");
            this.edtPass.requestFocus();
            return;
        }
        if(pass.length() < 6){
            this.edtPass.setError("Password should be atleast 6 length");
            this.edtPass.requestFocus();
            return;
        }

        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .userLogin(email, pass);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                LoginResponse result = response.body();
                Log.i(TAG, "onResponse: " + result.isError());
                if(!result.isError()){
                    SharedPref.getInstance(getBaseContext()).saveUser(result.getUser());

                    Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else
                    Toast.makeText(getBaseContext(), result.getMessage(), Toast.LENGTH_SHORT).show();

                //Toast.makeText(getBaseContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }
}