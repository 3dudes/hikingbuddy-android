package it.bz.its.angelhack.threedudes.hikingbuddy.services;

import it.bz.its.angelhack.threedudes.hikingbuddy.models.MissionSessionResponse;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface MissionSessionService {
    @POST("/mission_session.json")
    @FormUrlEncoded
    public void checkTag(@Query("user_id") String userId, @Field("mission_session[serial]") String tagId, Callback<MissionSessionResponse> cb);

    @DELETE("/mission_session.json")
    public void abortMission(@Query("user_id") String userId, Callback<Response> cb);
}
