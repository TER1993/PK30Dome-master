package com.speedata.pk30dome.database;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xuyan
 */
@Entity
public class HeavyDataBean implements Parcelable {

    //第一页的4个
    private String mQuotedPrice;
    private String mQuickReturn;
    private String mTypeOfGoods;
    private String mPackingType;


    //第二页的寄件人相关
    @Id
    private String mSenderOddNumber;


    @Generated(hash = 1787709296)
    public HeavyDataBean(String mQuotedPrice, String mQuickReturn,
            String mTypeOfGoods, String mPackingType, String mSenderOddNumber) {
        this.mQuotedPrice = mQuotedPrice;
        this.mQuickReturn = mQuickReturn;
        this.mTypeOfGoods = mTypeOfGoods;
        this.mPackingType = mPackingType;
        this.mSenderOddNumber = mSenderOddNumber;
    }


    @Generated(hash = 2022296110)
    public HeavyDataBean() {
    }


    public String getMQuotedPrice() {
        return this.mQuotedPrice;
    }


    public void setMQuotedPrice(String mQuotedPrice) {
        this.mQuotedPrice = mQuotedPrice;
    }


    public String getMQuickReturn() {
        return this.mQuickReturn;
    }


    public void setMQuickReturn(String mQuickReturn) {
        this.mQuickReturn = mQuickReturn;
    }


    public String getMTypeOfGoods() {
        return this.mTypeOfGoods;
    }


    public void setMTypeOfGoods(String mTypeOfGoods) {
        this.mTypeOfGoods = mTypeOfGoods;
    }


    public String getMPackingType() {
        return this.mPackingType;
    }


    public void setMPackingType(String mPackingType) {
        this.mPackingType = mPackingType;
    }


    public String getMSenderOddNumber() {
        return this.mSenderOddNumber;
    }


    public void setMSenderOddNumber(String mSenderOddNumber) {
        this.mSenderOddNumber = mSenderOddNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mQuotedPrice);
        dest.writeString(this.mQuickReturn);
        dest.writeString(this.mTypeOfGoods);
        dest.writeString(this.mPackingType);
        dest.writeString(this.mSenderOddNumber);
    }

    protected HeavyDataBean(Parcel in) {
        this.mQuotedPrice = in.readString();
        this.mQuickReturn = in.readString();
        this.mTypeOfGoods = in.readString();
        this.mPackingType = in.readString();
        this.mSenderOddNumber = in.readString();
    }

    public static final Parcelable.Creator<HeavyDataBean> CREATOR = new Parcelable.Creator<HeavyDataBean>() {
        @Override
        public HeavyDataBean createFromParcel(Parcel source) {
            return new HeavyDataBean(source);
        }

        @Override
        public HeavyDataBean[] newArray(int size) {
            return new HeavyDataBean[size];
        }
    };
}
