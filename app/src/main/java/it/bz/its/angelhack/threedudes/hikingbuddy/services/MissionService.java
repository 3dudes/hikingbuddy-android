package it.bz.its.angelhack.threedudes.hikingbuddy.services;

import it.bz.its.angelhack.threedudes.hikingbuddy.models.HeightGraph;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.MissionResponse;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.RankingResponse;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.Route;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface MissionService {
    @GET("/missions/search")
    public void getMission(@Query("serial") String nfcId, Callback<MissionResponse> cb);

    @GET("/missions/{mission_id}/route")
    public void getRoute(@Path("mission_id") int missionId, Callback<Route> cb);

    @GET("/missions/{mission_id}/height_profile")
    public void getHeightInfo(@Path("mission_id") int missionId, Callback<HeightGraph> cb);

    @GET("/mission/{mission_id}/ranking")
    public void getRanking(@Path("mission_id") int missionId, Callback<RankingResponse> cb);
}
