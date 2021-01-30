package com.smartElectronics.slim.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.smartElectronics.slim.R;
import com.smartElectronics.slim.activities.MainActivity;
import com.smartElectronics.slim.activities.ProfileActivity;
import com.smartElectronics.slim.network.RetrofitClient;
import com.smartElectronics.slim.network.model.DefaultResponse;
import com.smartElectronics.slim.network.model.UpdateResponse;
import com.smartElectronics.slim.network.model.User;
import com.smartElectronics.slim.storage.SharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener{

    private EditText edtEmail, edtName, edtSchool;
    private EditText edtOldPass, edtNewPass;

    private MaterialButton btnSaveProfile, btnChangePass;


    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SettingsFragment.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.settings_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.log_out:
                logOut();
                break;

            case R.id.del_user:
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setTitle("Attention!");
                builder.setMessage("Are you sure to delete your profile?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    deleteUer();
                });
                builder.setNegativeButton("No", (dialog, which) -> {

                });

                AlertDialog dialog = builder.create();
                dialog.show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(View view){

        edtEmail  = view.findViewById(R.id.edt_email);
        edtName   = view.findViewById(R.id.edt_name);
        edtSchool = view.findViewById(R.id.edt_school);

        edtOldPass = view.findViewById(R.id.old_pass);
        edtNewPass = view.findViewById(R.id.new_pass);

        btnSaveProfile = view.findViewById(R.id.btn_save);
        btnChangePass  = view.findViewById(R.id.btn_change_pass);

        btnSaveProfile.setOnClickListener(this);
        btnChangePass.setOnClickListener(this);
    }

    private void updateProfile() {

        String email  = this.edtEmail.getText().toString().trim();
        String name   = this.edtName.getText().toString().trim();
        String school = this.edtSchool.getText().toString().trim();

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

        if(name.isEmpty()){
            this.edtName.setError("name is required");
            this.edtName.requestFocus();
            return;
        }
        if(school.isEmpty()){
            this.edtSchool.setError("school is required");
            this.edtSchool.requestFocus();
            return;
        }


        User user = SharedPref.getInstance(getContext()).getUser();
        Call<UpdateResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateUser(user.getId(), email, name, school);

        call.enqueue(new Callback<UpdateResponse>() {
            @Override
            public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {

                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                if(!response.body().isError())
                    SharedPref.getInstance(getContext()).saveUser(response.body().getUser());
            }

            @Override
            public void onFailure(Call<UpdateResponse> call, Throwable t) {

            }
        });
    }
    private void updatePassword(){

        String oldPass = edtOldPass.getText().toString().trim();
        String newPass = edtNewPass.getText().toString().trim();

        if(oldPass.isEmpty()){
            edtOldPass.setError("password is required");
            edtOldPass.requestFocus();
            return;
        }

        if(!newPass.isEmpty()){
            if(newPass.length() < 6){
                edtNewPass.setError("password must be 6 character or more");
                edtNewPass.requestFocus();
                return;
            }
        }else{
            edtNewPass.setError("password is required");
            edtNewPass.requestFocus();
            return;
        }

        User user = SharedPref.getInstance(getContext()).getUser();

        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .updatePass(oldPass, newPass, user.getEmail());

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }
    private void logOut(){

        SharedPref.getInstance(getContext()).clear();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void deleteUer() {

        User user = SharedPref.getInstance(getContext()).getUser();
        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .removeUser(user.getId());

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                if(!response.body().isError()){
                    SharedPref.getInstance(getContext()).clear();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_save:
                updateProfile();
                break;

            case R.id.btn_change_pass:
                updatePassword();
                break;
        }
    }


}