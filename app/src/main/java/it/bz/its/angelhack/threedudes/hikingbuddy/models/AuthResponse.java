package it.bz.its.angelhack.threedudes.hikingbuddy.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AuthResponse implements Parcelable {
    Auth authToken;

    public AuthResponse(Parcel in) {
        this.authToken = (Auth) in.readParcelable(Auth.class.getClassLoader());
    }

   public Auth getAuthToken() {
       return this.authToken;
   }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(authToken, 0);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AuthResponse createFromParcel(Parcel in) {
            return new AuthResponse(in);
        }

        public AuthResponse[] newArray(int size) {
            return new AuthResponse[size];
        }
    };
}
