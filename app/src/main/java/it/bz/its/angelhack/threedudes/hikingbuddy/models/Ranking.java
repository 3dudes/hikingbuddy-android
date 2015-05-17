package it.bz.its.angelhack.threedudes.hikingbuddy.models;

import java.util.Date;

public class Ranking {
    int id;
    Date startedAt;
    Date completedAt;
    String status;
    int missionId;
    int score;
    User user;

    public User getUser() {
        return this.user;
    }
}
