package doesthiswork;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    private final int foo;

    public Item(int foo) {
        this.foo = foo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(foo);
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {

        @Override
        public Item createFromParcel(Parcel parcel) {
            return new Item(parcel.readInt());
        }

        @Override
        public Item[] newArray(int i) {
            return new Item[i];
        }
    };
}
