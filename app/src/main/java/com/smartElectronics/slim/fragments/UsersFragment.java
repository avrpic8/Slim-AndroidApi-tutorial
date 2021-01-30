package com.smartElectronics.slim.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.material.snackbar.Snackbar;
import com.smartElectronics.slim.R;
import com.smartElectronics.slim.adapters.UserAdapter;
import com.smartElectronics.slim.network.RetrofitClient;
import com.smartElectronics.slim.network.model.User;
import com.smartElectronics.slim.network.model.UsersResponse;
import com.smartElectronics.slim.storage.SharedPref;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment {

    private RecyclerView usersList;
    private UserAdapter userAdapter;
    private List<User> users;
    private String TAG = "users";


    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecycler(view);
    }

    private void initRecycler(View view){

        usersList = view.findViewById(R.id.list_users);
        usersList.setLayoutManager(new LinearLayoutManager(getActivity()));

        Call<UsersResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getAllUsers();

        call.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {

                if(response.code() == 200) {
                    users = response.body().getUsers();
                    userAdapter = new UserAdapter(getContext(), users);
                    usersList.setAdapter(userAdapter);
                }else {
                    try {
                        Snackbar.make(getView(), response.errorBody().string(), Snackbar.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {

                Snackbar.make(getView(), "Cant connect to server...", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}