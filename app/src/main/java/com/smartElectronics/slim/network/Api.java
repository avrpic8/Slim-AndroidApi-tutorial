package com.smartElectronics.slim.network;

import com.smartElectronics.slim.network.model.DefaultResponse;
import com.smartElectronics.slim.network.model.LoginResponse;
import com.smartElectronics.slim.network.model.UpdateResponse;
import com.smartElectronics.slim.network.model.UsersResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {

    @FormUrlEncoded
    @POST("createUser")
    Call<DefaultResponse> createUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String name,
            @Field("school") String school
    );

    @FormUrlEncoded
    @POST("userLogin")
    Call<LoginResponse> userLogin(
        @Field("email") String email,
        @Field("password") String password
    );


    @GET("allUsers")
    Call<UsersResponse> getAllUsers();

    @FormUrlEncoded
    @PUT("updateUser/{id}")
    Call<UpdateResponse> updateUser(
            @Path("id") int id,
            @Field("email") String email,
            @Field("name") String name,
            @Field("school") String school
    );

    @FormUrlEncoded
    @PUT("updatePass")
    Call<DefaultResponse> updatePass(
            @Field("currentPass") String oldPass,
            @Field("newPass") String newPass,
            @Field("email") String email
    );

    @DELETE("deleteUser/{id}")
    Call<DefaultResponse> removeUser(@Path("id") int id);


}
