package it.bz.its.angelhack.threedudes.hikingbuddy.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Picture implements Parcelable {
    @SerializedName("default")
    String normalImageUri;
    @SerializedName("thumb")
    String thumbImageUri;

    public Picture(Parcel in) {
        this.normalImageUri = in.readString();
        this.thumbImageUri = in.readString();
    }

    public String getNormalImageUri() {
        return this.normalImageUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.normalImageUri);
        dest.writeString(this.thumbImageUri);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Picture createFromParcel(Parcel in) {
            return new Picture(in);
        }

        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };
}
