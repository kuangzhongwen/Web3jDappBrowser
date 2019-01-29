package io.web3j.browser;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Address wrapper entity.
 *
 * @author kuang on 2018/01/08.
 */
public final class Address implements Parcelable {

    public static final Address EMPTY = new Address("0000000000000000000000000000000000000000");

    private final String value;

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public Address(@NonNull String value) {
        value = value.toLowerCase();
        if (Hex.containsHexPrefix(value)) {
            value = Hex.cleanHexPrefix(value);
        }
        if (TextUtils.isEmpty(value)) {
            throw new IllegalArgumentException("Address can't null.");
        } else {
            this.value = value;
        }
    }

    protected Address(Parcel in) {
        this.value = in.readString();
    }

    public String toString() {
        return "0x" + this.value;
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public boolean equals(Object other) {
        return other instanceof Address && this.value.equals(((Address) other).value);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.value);
    }
}
