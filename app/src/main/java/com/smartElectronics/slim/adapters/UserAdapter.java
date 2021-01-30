package com.smartElectronics.slim.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartElectronics.slim.R;
import com.smartElectronics.slim.network.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {


    private Context context;
    private List<User> users;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recycler_users, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        User user = users.get(position);

        holder.email.setText(user.getEmail());
        holder.name.setText(user.getName());
        holder.school.setText(user.getSchool());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView email, name, school;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            email  = itemView.findViewById(R.id.txt_email);
            name   = itemView.findViewById(R.id.txt_name);
            school = itemView.findViewById(R.id.txt_school);
        }
    }
}
