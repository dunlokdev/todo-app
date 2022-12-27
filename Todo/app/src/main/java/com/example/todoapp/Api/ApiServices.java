package com.example.todoapp.Api;

import com.example.todoapp.Model.ResponseModel;
import com.example.todoapp.Model.TodoModel;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ApiServices {
    Interceptor interceptor = chain ->
    {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.addHeader("Content-Type", "application/json");

        return chain.proceed(builder.build());
    };

    OkHttpClient.Builder okBuilder = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(interceptor);

    ApiServices apiServices = new Retrofit.Builder()
            .baseUrl("http://192.168.0.4:3000/api/v1/tasks/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okBuilder.build())
            .build()
            .create(ApiServices.class);

    @GET(".")
    Call<ResponseModel> GetAllTasks(@QueryMap Map<String, String> options);

    @POST("store")
    Call<TodoModel> AddTask(@Body TodoModel todoModel);

    @PUT("update/{id}")
    Call<TodoModel> UpdateTask(@Path("id") String id, @Body TodoModel todoModel);

    @PATCH("update/{id}")
    Call<TodoModel> SwitchStatus(@Path("id") String id, @Body TodoModel todoModel);

    @DELETE("delete/{id}")
    Call<Void> DeleteTask(@Path("id") String id);
}
