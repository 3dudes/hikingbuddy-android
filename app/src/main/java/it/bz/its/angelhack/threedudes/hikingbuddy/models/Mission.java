package it.bz.its.angelhack.threedudes.hikingbuddy.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.annotations.SerializedName;

public class Mission implements Parcelable {
    int id;
    String name;
    int distance;
    int averageTime;
    int sessionCountTotal;
    Location startLocation;
    Location endLocation;

    public Mission(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.distance = in.readInt();
        this.averageTime = in.readInt();
        this.sessionCountTotal = in.readInt();
        this.startLocation = (Location)in.readParcelable(Location.class.getClassLoader());
        this.endLocation = (Location)in.readParcelable(Location.class.getClassLoader());
    }

    public int getId() { return this.id; }
    public String getName() {
        return this.name;
    }

    public int getDistance() {
        return this.distance;
    }
    public int getAverageTime() {
        return  this.averageTime;
    }

    public Location getStartLocation() {
        return this.startLocation;
    }
    public Location getEndLocation() {
        return this.endLocation;
    }

    public Location[] getImportantPoints() {
        return new Location[]{this.startLocation, this.endLocation};
    }

    public LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (Location pos : getImportantPoints()) {
            builder.include(pos.getGoogleMapCoord());
        }

        return builder.build();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.distance);
        dest.writeInt(this.averageTime);
        dest.writeInt(this.sessionCountTotal);
        dest.writeParcelable(this.startLocation, 0);
        dest.writeParcelable(this.endLocation, 0);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Mission createFromParcel(Parcel in) {
            return new Mission(in);
        }

        public Mission[] newArray(int size) {
            return new Mission[size];
        }
    };
}
