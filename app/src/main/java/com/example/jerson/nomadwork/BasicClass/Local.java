package com.example.jerson.nomadwork.BasicClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Blob;

public class Local implements Parcelable {
    int localId;
    String localName;
    Double latitude;
    Double longitude;
    String internet;
    String price;
    String energy;
    String noise;


    Blob photo;
    String nameCreator;

    public Local() {
    }


    protected Local(Parcel in) {
        localId = in.readInt();
        localName = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        internet = in.readString();
        price = in.readString();
        energy = in.readString();
        noise = in.readString();
        nameCreator = in.readString();
    }

    public static final Creator<Local> CREATOR = new Creator<Local>() {
        @Override
        public Local createFromParcel(Parcel in) {
            return new Local( in );
        }

        @Override
        public Local[] newArray(int size) {
            return new Local[size];
        }
    };

    public String getInternet() {
        return internet;
    }

    public void setInternet(String internet) {
        this.internet = internet;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }

    public String getNoise() {
        return noise;
    }

    public void setNoise(String noise) {
        this.noise = noise;
    }


    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Blob getPhoto() {
        return photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }

    public String getNameCreator() {
        return nameCreator;
    }

    public void setNameCreator(String nameCreator) {
        this.nameCreator = nameCreator;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt( localId );
        dest.writeString( localName );
        if (latitude == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeDouble( latitude );
        }
        if (longitude == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeDouble( longitude );
        }
        dest.writeString( internet );
        dest.writeString( price );
        dest.writeString( energy );
        dest.writeString( noise );
        dest.writeString( nameCreator );
    }
}
