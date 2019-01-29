package io.web3j.browser;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Web browser message entity
 *
 * @author kuang on 2018/01/08.
 */
public final class Message<V> implements Parcelable {

    public final V value;
    public final String url;
    public final long leafPosition;

    public static final Creator<Message> CREATOR = new Creator<Message>() {

        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public Message(V value, String url, long leafPosition) {
        this.value = value;
        this.url = url;
        this.leafPosition = leafPosition;
    }

    @SuppressWarnings("unchecked")
    protected Message(Parcel in) {
        Class<?> type = (Class) in.readSerializable();
        this.value = (V) in.readValue(type.getClassLoader());
        this.url = in.readString();
        this.leafPosition = in.readLong();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.value.getClass());
        dest.writeValue(this.value);
        dest.writeString(this.url);
        dest.writeLong(this.leafPosition);
    }
}
