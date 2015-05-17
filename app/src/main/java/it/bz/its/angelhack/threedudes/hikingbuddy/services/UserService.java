package it.bz.its.angelhack.threedudes.hikingbuddy.services;

import it.bz.its.angelhack.threedudes.hikingbuddy.models.UserResponse;
import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

public interface UserService {
    @POST("")
    public void login(@Query("username") String user, @Query("password") String passwd, Callback<UserResponse> cb);
}
