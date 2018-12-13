package com.sports.sportclub.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BmobService {

    @Headers({"X-Bmob-Application-Id: 24d2dd9a00667b645f55acaef2d11e16",
            "X-Bmob-REST-API-Key: 22cbfdb0d32d41015385badae3d03b01",})
    @GET("/1/login/")
    Call<ResponseBody> getUser(@Query("username") String username, @Query("password") String password);

    @Headers({"X-Bmob-Application-Id: 24d2dd9a00667b645f55acaef2d11e16",
            "X-Bmob-REST-API-Key: 22cbfdb0d32d41015385badae3d03b01",
            "Content-Type: application/json"})
    @POST("/1/users")
    Call<ResponseBody> postUser(@Body RequestBody body);

}
