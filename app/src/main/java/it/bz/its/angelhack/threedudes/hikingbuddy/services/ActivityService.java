package it.bz.its.angelhack.threedudes.hikingbuddy.services;

import it.bz.its.angelhack.threedudes.hikingbuddy.models.ActivitiesResponse;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by philipgiuliani on 17.05.15.
 */
public interface ActivityService {
    @GET("/activities")
    void getActivities(Callback<ActivitiesResponse> cb);

}
