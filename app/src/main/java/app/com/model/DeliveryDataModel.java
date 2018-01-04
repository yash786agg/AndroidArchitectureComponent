package app.com.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/*
 * Created by Yash on 4/1/18.
 */

@Entity//Entity represents a class that holds a database row For each entity, a database table is created to hold the items.
public class DeliveryDataModel implements Parcelable
{
    @PrimaryKey(autoGenerate = true)
    private int slNo;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "imageUrl")
    private String imageUrl;

    @ColumnInfo(name = "lat")
    private double lat;

    @ColumnInfo(name = "lng")
    private double lng;

    @ColumnInfo(name = "address")
    private String address;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public DeliveryDataModel()
    { }

    public int getSlNo() {
        return slNo;
    }

    public void setSlNo(int slNo) {
        this.slNo = slNo;
    }

    @Override
    public String toString()
    {
        return "Products_Pojo{" +
                "slNo='" + slNo + '\'' +
                "imageUrl='" + imageUrl + '\'' +
                "address='" + address + '\'' +
                "lng='" + lng + '\'' +
                "lat='" + lat + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(slNo);
        dest.writeString(address);
        dest.writeDouble(lng);
        dest.writeDouble(lat);
        dest.writeString(imageUrl);
        dest.writeString(description);
    }

    public DeliveryDataModel(Parcel in)
    {
        slNo = in.readInt();
        address = in.readString();
        lng = in.readDouble();
        lat = in.readDouble();
        imageUrl = in.readString();
        description = in.readString();
    }

    public static final Creator<DeliveryDataModel> CREATOR = new Creator<DeliveryDataModel>() {
        @Override
        public DeliveryDataModel createFromParcel(Parcel in) {
            return new DeliveryDataModel(in);
        }

        @Override
        public DeliveryDataModel[] newArray(int size) {
            return new DeliveryDataModel[size];
        }
    };
}
