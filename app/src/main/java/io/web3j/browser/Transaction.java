package io.web3j.browser;

import android.os.Parcel;
import android.os.Parcelable;
import java.math.BigInteger;

/**
 * Transaction entity
 *
 * @author kuang on 2018/01/08.
 */
public final class Transaction implements Parcelable {

    public final Address recipient;
    public final Address contract;
    public final BigInteger value;
    public final BigInteger gasPrice;
    public final long gasLimit;
    public final long nonce;
    public final String payload;
    public final long leafPosition;

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public Transaction(Address recipient, Address contract, BigInteger value, BigInteger gasPrice, long gasLimit, long nonce, String payload) {
        this(recipient, contract, value, gasPrice, gasLimit, nonce, payload, 0L);
    }

    public Transaction(Address recipient, Address contract, BigInteger value, BigInteger gasPrice, long gasLimit, long nonce, String payload, long leafPosition) {
        this.recipient = recipient;
        this.contract = contract;
        this.value = value;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.nonce = nonce;
        this.payload = payload;
        this.leafPosition = leafPosition;
    }

    Transaction(Parcel in) {
        this.recipient = (Address) in.readParcelable(Address.class.getClassLoader());
        this.contract = (Address) in.readParcelable(Address.class.getClassLoader());
        this.value = new BigInteger(in.readString());
        this.gasPrice = new BigInteger(in.readString());
        this.gasLimit = in.readLong();
        this.nonce = in.readLong();
        this.payload = in.readString();
        this.leafPosition = in.readLong();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.recipient, flags);
        dest.writeParcelable(this.contract, flags);
        dest.writeString((this.value == null ? BigInteger.ZERO : this.value).toString());
        dest.writeString((this.gasPrice == null ? BigInteger.ZERO : this.gasPrice).toString());
        dest.writeLong(this.gasLimit);
        dest.writeLong(this.nonce);
        dest.writeString(this.payload);
        dest.writeLong(this.leafPosition);
    }
}

