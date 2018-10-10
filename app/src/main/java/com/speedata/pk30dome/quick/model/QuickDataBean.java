package com.speedata.pk30dome.quick.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author xuyan  全部数据
 */
public class QuickDataBean implements Parcelable {

    //第一页的4个
    private String mQuotedPrice;
    private String mQuickReturn;
    private String mTypeOfGoods;
    private String mPackingType;
    private List<QuickBean> mList;

    //第二页的寄件人相关
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

    public String getmQuotedPrice() {
        return mQuotedPrice;
    }

    public void setmQuotedPrice(String mQuotedPrice) {
        this.mQuotedPrice = mQuotedPrice;
    }

    public String getmQuickReturn() {
        return mQuickReturn;
    }

    public void setmQuickReturn(String mQuickReturn) {
        this.mQuickReturn = mQuickReturn;
    }

    public String getmTypeOfGoods() {
        return mTypeOfGoods;
    }

    public void setmTypeOfGoods(String mTypeOfGoods) {
        this.mTypeOfGoods = mTypeOfGoods;
    }

    public String getmPackingType() {
        return mPackingType;
    }

    public void setmPackingType(String mPackingType) {
        this.mPackingType = mPackingType;
    }

    public List<QuickBean> getmList() {
        return mList;
    }

    public void setmList(List<QuickBean> mList) {
        this.mList = mList;
    }

    public String getmSenderOddNumber() {
        return mSenderOddNumber;
    }

    public void setmSenderOddNumber(String mSenderOddNumber) {
        this.mSenderOddNumber = mSenderOddNumber;
    }

    public String getmSenderTheSender() {
        return mSenderTheSender;
    }

    public void setmSenderTheSender(String mSenderTheSender) {
        this.mSenderTheSender = mSenderTheSender;
    }

    public String getmSenderPhoneNumber() {
        return mSenderPhoneNumber;
    }

    public void setmSenderPhoneNumber(String mSenderPhoneNumber) {
        this.mSenderPhoneNumber = mSenderPhoneNumber;
    }

    public String getmSenderCompany() {
        return mSenderCompany;
    }

    public void setmSenderCompany(String mSenderCompany) {
        this.mSenderCompany = mSenderCompany;
    }

    public String getmSenderAddress() {
        return mSenderAddress;
    }

    public void setmSenderAddress(String mSenderAddress) {
        this.mSenderAddress = mSenderAddress;
    }

    public String getmCollectionTheSender() {
        return mCollectionTheSender;
    }

    public void setmCollectionTheSender(String mCollectionTheSender) {
        this.mCollectionTheSender = mCollectionTheSender;
    }

    public String getmCollectionPhoneNumber() {
        return mCollectionPhoneNumber;
    }

    public void setmCollectionPhoneNumber(String mCollectionPhoneNumber) {
        this.mCollectionPhoneNumber = mCollectionPhoneNumber;
    }

    public String getmCollectionCompany() {
        return mCollectionCompany;
    }

    public void setmCollectionCompany(String mCollectionCompany) {
        this.mCollectionCompany = mCollectionCompany;
    }

    public String getmCollectionAddress() {
        return mCollectionAddress;
    }

    public void setmCollectionAddress(String mCollectionAddress) {
        this.mCollectionAddress = mCollectionAddress;
    }

    @Override
    public String toString() {
        return "QuickDataBean{" +
                "mQuotedPrice='" + mQuotedPrice + '\'' +
                ", mQuickReturn='" + mQuickReturn + '\'' +
                ", mTypeOfGoods='" + mTypeOfGoods + '\'' +
                ", mPackingType='" + mPackingType + '\'' +
                ", mList=" + mList +
                ", mSenderOddNumber='" + mSenderOddNumber + '\'' +
                ", mSenderTheSender='" + mSenderTheSender + '\'' +
                ", mSenderPhoneNumber='" + mSenderPhoneNumber + '\'' +
                ", mSenderCompany='" + mSenderCompany + '\'' +
                ", mSenderAddress='" + mSenderAddress + '\'' +
                ", mCollectionTheSender='" + mCollectionTheSender + '\'' +
                ", mCollectionPhoneNumber='" + mCollectionPhoneNumber + '\'' +
                ", mCollectionCompany='" + mCollectionCompany + '\'' +
                ", mCollectionAddress='" + mCollectionAddress + '\'' +
                '}';
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
        dest.writeTypedList(this.mList);
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

    public QuickDataBean() {
    }

    protected QuickDataBean(Parcel in) {
        this.mQuotedPrice = in.readString();
        this.mQuickReturn = in.readString();
        this.mTypeOfGoods = in.readString();
        this.mPackingType = in.readString();
        this.mList = in.createTypedArrayList(QuickBean.CREATOR);
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
