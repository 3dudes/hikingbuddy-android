package it.bz.its.angelhack.threedudes.hikingbuddy.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Location extends RoutePoint implements Parcelable {
    int id;
    String name;

    public Location(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
