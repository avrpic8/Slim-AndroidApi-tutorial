package com.smartElectronics.slim.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartElectronics.slim.R;
import com.smartElectronics.slim.storage.SharedPref;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private TextView txtEmail, txtName, txtSchool;

    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }


    private void initViews(View view){

        txtEmail  = view.findViewById(R.id.txt_viewEmail);
        txtName   = view.findViewById(R.id.txt_viewName);
        txtSchool = view.findViewById(R.id.txt_viewSchool);

        txtEmail.setText(SharedPref.getInstance(getActivity()).getUser().getEmail());
        txtName.setText(SharedPref.getInstance(getActivity()).getUser().getName());
        txtSchool.setText(SharedPref.getInstance(getActivity()).getUser().getSchool());
    }
}