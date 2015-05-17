package it.bz.its.angelhack.threedudes.hikingbuddy.services;

import it.bz.its.angelhack.threedudes.hikingbuddy.models.AuthResponse;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Query;

public interface UserService {
    @POST("/session.json")
    @FormUrlEncoded
    public void login(@Field("session[email]") String email, @Field("session[password]") String passwd, Callback<AuthResponse> cb);
}
