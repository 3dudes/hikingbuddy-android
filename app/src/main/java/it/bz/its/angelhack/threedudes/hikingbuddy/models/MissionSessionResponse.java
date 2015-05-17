package it.bz.its.angelhack.threedudes.hikingbuddy.models;

import com.google.gson.annotations.SerializedName;

public class MissionSessionResponse {
    @SerializedName("mission_name")
    MissionSession ms;

    public MissionSession getMissionSession() {
        return this.ms;
    }
}
