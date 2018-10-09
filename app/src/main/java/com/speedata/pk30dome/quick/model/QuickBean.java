package com.speedata.pk30dome.quick.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author xuyan
 */
public class QuickBean implements Parcelable {

    private String actualWeight;
    private String bubbleWeight;
    private String cargoSize;
    private String quickNumber;

    public String getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(String actualWeight) {
        this.actualWeight = actualWeight;
    }

    public String getBubbleWeight() {
        return bubbleWeight;
    }

    public void setBubbleWeight(String bubbleWeight) {
        this.bubbleWeight = bubbleWeight;
    }

    public String getCargoSize() {
        return cargoSize;
    }

    public void setCargoSize(String cargoSize) {
        this.cargoSize = cargoSize;
    }

    public String getQuickNumber() {
        return quickNumber;
    }

    public void setQuickNumber(String quickNumber) {
        this.quickNumber = quickNumber;
    }

    @Override
    public String toString() {
        return "QuickBean{" +
                "actualWeight='" + actualWeight + '\'' +
                ", bubbleWeight='" + bubbleWeight + '\'' +
                ", cargoSize='" + cargoSize + '\'' +
                ", quickNumber='" + quickNumber + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.actualWeight);
        dest.writeString(this.bubbleWeight);
        dest.writeString(this.cargoSize);
        dest.writeString(this.quickNumber);
    }

    public QuickBean() {
    }

    protected QuickBean(Parcel in) {
        this.actualWeight = in.readString();
        this.bubbleWeight = in.readString();
        this.cargoSize = in.readString();
        this.quickNumber = in.readString();
    }

    public static final Parcelable.Creator<QuickBean> CREATOR = new Parcelable.Creator<QuickBean>() {
        @Override
        public QuickBean createFromParcel(Parcel source) {
            return new QuickBean(source);
        }

        @Override
        public QuickBean[] newArray(int size) {
            return new QuickBean[size];
        }
    };
}
