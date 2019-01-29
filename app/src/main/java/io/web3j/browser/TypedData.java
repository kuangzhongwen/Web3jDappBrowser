package io.web3j.browser;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Web browser typed data entity
 *
 * @author kuang on 2018/01/08.
 */
public final class TypedData implements Parcelable {

    public final String name;
    public final String type;
    public final Object data;

    public static final Creator<TypedData> CREATOR = new Creator<TypedData>() {
        public TypedData createFromParcel(Parcel in) {
            return new TypedData(in);
        }

        public TypedData[] newArray(int size) {
            return new TypedData[size];
        }
    };

    public TypedData(String name, String type, Object data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

    protected TypedData(Parcel in) {
        this.name = in.readString();
        this.type = in.readString();
        Class<?> type = (Class) in.readSerializable();
        this.data = in.readValue(type.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeSerializable(this.data.getClass());
        dest.writeValue(this.data);
    }
}
