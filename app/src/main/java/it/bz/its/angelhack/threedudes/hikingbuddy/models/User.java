package it.bz.its.angelhack.threedudes.hikingbuddy.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    int id;
    String firstName;
    String lastName;
    Picture picture;

    public User(Parcel in) {
        this.id = in.readInt();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.picture = (Picture) in.readParcelable(Picture.class.getClassLoader());
    }

    public String getFirstName() {
        return this.firstName;
    }
    public Picture getPicture() {
        return this.picture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeParcelable(this.picture, 0);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
