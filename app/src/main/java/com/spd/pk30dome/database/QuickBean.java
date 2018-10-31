package com.spd.pk30dome.database;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xuyan
 */
@Entity
public class QuickBean implements Parcelable {

    @Id
    private Long id;

    private String mSenderOddNumber;
    private String actualWeight;
    private String bubbleWeight;
    private String cargoSize;
    private String quickNumber;
    @Generated(hash = 606361990)
    public QuickBean(Long id, String mSenderOddNumber, String actualWeight,
            String bubbleWeight, String cargoSize, String quickNumber) {
        this.id = id;
        this.mSenderOddNumber = mSenderOddNumber;
        this.actualWeight = actualWeight;
        this.bubbleWeight = bubbleWeight;
        this.cargoSize = cargoSize;
        this.quickNumber = quickNumber;
    }
    @Generated(hash = 578731038)
    public QuickBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMSenderOddNumber() {
        return this.mSenderOddNumber;
    }
    public void setMSenderOddNumber(String mSenderOddNumber) {
        this.mSenderOddNumber = mSenderOddNumber;
    }
    public String getActualWeight() {
        return this.actualWeight;
    }
    public void setActualWeight(String actualWeight) {
        this.actualWeight = actualWeight;
    }
    public String getBubbleWeight() {
        return this.bubbleWeight;
    }
    public void setBubbleWeight(String bubbleWeight) {
        this.bubbleWeight = bubbleWeight;
    }
    public String getCargoSize() {
        return this.cargoSize;
    }
    public void setCargoSize(String cargoSize) {
        this.cargoSize = cargoSize;
    }
    public String getQuickNumber() {
        return this.quickNumber;
    }
    public void setQuickNumber(String quickNumber) {
        this.quickNumber = quickNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.mSenderOddNumber);
        dest.writeString(this.actualWeight);
        dest.writeString(this.bubbleWeight);
        dest.writeString(this.cargoSize);
        dest.writeString(this.quickNumber);
    }

    protected QuickBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.mSenderOddNumber = in.readString();
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
