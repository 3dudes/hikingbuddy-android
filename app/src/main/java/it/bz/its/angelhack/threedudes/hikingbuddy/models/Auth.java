package it.bz.its.angelhack.threedudes.hikingbuddy.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Auth implements Parcelable {
    String token;
    User user;

    public Auth(Parcel in) {
        token = in.readString();
        this.user = (User) in.readParcelable(User.class.getClassLoader());
    }

    public String getToken() {
        return this.token;
    }
    public User getUser() { return this.user; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        dest.writeParcelable(user, 0);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Auth createFromParcel(Parcel in) {
            return new Auth(in);
        }

        public Auth[] newArray(int size) {
            return new Auth[size];
        }
    };
}
