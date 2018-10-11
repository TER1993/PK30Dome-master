package com.speedata.pk30dome.database;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xuyan  全部数据
 */
@Entity
public class QuickDataBean implements Parcelable {

    //第一页的4个
    private String mQuotedPrice;
    private String mQuickReturn;
    private String mTypeOfGoods;
    private String mPackingType;


    //第二页的寄件人相关
    @Id
    private String mSenderOddNumber;
    private String mSenderTheSender;
    private String mSenderPhoneNumber;
    private String mSenderCompany;
    private String mSenderAddress;

    //第三页收件人相关
    private String mCollectionTheSender;
    private String mCollectionPhoneNumber;
    private String mCollectionCompany;
    private String mCollectionAddress;
    @Generated(hash = 615574538)
    public QuickDataBean(String mQuotedPrice, String mQuickReturn,
            String mTypeOfGoods, String mPackingType, String mSenderOddNumber,
            String mSenderTheSender, String mSenderPhoneNumber,
            String mSenderCompany, String mSenderAddress,
            String mCollectionTheSender, String mCollectionPhoneNumber,
            String mCollectionCompany, String mCollectionAddress) {
        this.mQuotedPrice = mQuotedPrice;
        this.mQuickReturn = mQuickReturn;
        this.mTypeOfGoods = mTypeOfGoods;
        this.mPackingType = mPackingType;
        this.mSenderOddNumber = mSenderOddNumber;
        this.mSenderTheSender = mSenderTheSender;
        this.mSenderPhoneNumber = mSenderPhoneNumber;
        this.mSenderCompany = mSenderCompany;
        this.mSenderAddress = mSenderAddress;
        this.mCollectionTheSender = mCollectionTheSender;
        this.mCollectionPhoneNumber = mCollectionPhoneNumber;
        this.mCollectionCompany = mCollectionCompany;
        this.mCollectionAddress = mCollectionAddress;
    }
    @Generated(hash = 1472387597)
    public QuickDataBean() {
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
    public String getMSenderTheSender() {
        return this.mSenderTheSender;
    }
    public void setMSenderTheSender(String mSenderTheSender) {
        this.mSenderTheSender = mSenderTheSender;
    }
    public String getMSenderPhoneNumber() {
        return this.mSenderPhoneNumber;
    }
    public void setMSenderPhoneNumber(String mSenderPhoneNumber) {
        this.mSenderPhoneNumber = mSenderPhoneNumber;
    }
    public String getMSenderCompany() {
        return this.mSenderCompany;
    }
    public void setMSenderCompany(String mSenderCompany) {
        this.mSenderCompany = mSenderCompany;
    }
    public String getMSenderAddress() {
        return this.mSenderAddress;
    }
    public void setMSenderAddress(String mSenderAddress) {
        this.mSenderAddress = mSenderAddress;
    }
    public String getMCollectionTheSender() {
        return this.mCollectionTheSender;
    }
    public void setMCollectionTheSender(String mCollectionTheSender) {
        this.mCollectionTheSender = mCollectionTheSender;
    }
    public String getMCollectionPhoneNumber() {
        return this.mCollectionPhoneNumber;
    }
    public void setMCollectionPhoneNumber(String mCollectionPhoneNumber) {
        this.mCollectionPhoneNumber = mCollectionPhoneNumber;
    }
    public String getMCollectionCompany() {
        return this.mCollectionCompany;
    }
    public void setMCollectionCompany(String mCollectionCompany) {
        this.mCollectionCompany = mCollectionCompany;
    }
    public String getMCollectionAddress() {
        return this.mCollectionAddress;
    }
    public void setMCollectionAddress(String mCollectionAddress) {
        this.mCollectionAddress = mCollectionAddress;
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
        dest.writeString(this.mSenderTheSender);
        dest.writeString(this.mSenderPhoneNumber);
        dest.writeString(this.mSenderCompany);
        dest.writeString(this.mSenderAddress);
        dest.writeString(this.mCollectionTheSender);
        dest.writeString(this.mCollectionPhoneNumber);
        dest.writeString(this.mCollectionCompany);
        dest.writeString(this.mCollectionAddress);
    }

    protected QuickDataBean(Parcel in) {
        this.mQuotedPrice = in.readString();
        this.mQuickReturn = in.readString();
        this.mTypeOfGoods = in.readString();
        this.mPackingType = in.readString();
        this.mSenderOddNumber = in.readString();
        this.mSenderTheSender = in.readString();
        this.mSenderPhoneNumber = in.readString();
        this.mSenderCompany = in.readString();
        this.mSenderAddress = in.readString();
        this.mCollectionTheSender = in.readString();
        this.mCollectionPhoneNumber = in.readString();
        this.mCollectionCompany = in.readString();
        this.mCollectionAddress = in.readString();
    }

    public static final Parcelable.Creator<QuickDataBean> CREATOR = new Parcelable.Creator<QuickDataBean>() {
        @Override
        public QuickDataBean createFromParcel(Parcel source) {
            return new QuickDataBean(source);
        }

        @Override
        public QuickDataBean[] newArray(int size) {
            return new QuickDataBean[size];
        }
    };
}
