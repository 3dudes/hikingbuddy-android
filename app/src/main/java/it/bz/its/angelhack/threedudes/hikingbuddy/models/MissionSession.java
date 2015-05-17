package it.bz.its.angelhack.threedudes.hikingbuddy.models;

import java.util.Date;

public class MissionSession {
    int id;
    Date startedAt;
    Date completedAt;
    String status;

    // Used for requests
    String serial;

    public MissionSession(String tagId) {
        this.serial = tagId;
    }
}
