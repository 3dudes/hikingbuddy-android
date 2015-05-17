package it.bz.its.angelhack.threedudes.hikingbuddy.services;

import it.bz.its.angelhack.threedudes.hikingbuddy.models.MissionResponse;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.Route;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface MissionService {
    @GET("/missions/search")
    public void getMission(@Query("user_id") String userId, @Query("serial") String nfcId, Callback<MissionResponse> cb);

    @GET("/missions/{mission_id}/route?user_id=1")
    public void getRoute(@Path("mission_id") int missionId, @Query("user_id") String userId, Callback<Route> cb);
}
