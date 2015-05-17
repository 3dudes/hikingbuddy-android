package it.bz.its.angelhack.threedudes.hikingbuddy.models;

import java.util.Date;

public class MissionSession {
    int id;
    Date startedAt;
    Date completedAt;
    String status;
    User user;
    Mission mission;
    int score;

    // Used for requests
    String serial;

    public MissionSession(String tagId) {
        this.serial = tagId;
    }

    public User getUser() {
        return this.user;
    }

    public Date getCompletedAt() {
        return this.completedAt;
    }

    public Date getStartedAt() {
        return this.startedAt;
    }

    public Mission getMission() {
        return this.mission;
    }

    public int getScore() {
        return this.score;
    }
}
