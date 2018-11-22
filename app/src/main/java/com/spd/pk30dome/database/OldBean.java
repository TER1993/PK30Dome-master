package com.spd.pk30dome.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author xuyan    old 原页面部分
 */
@Entity
public class OldBean {
    //第一页的4个
    private String actualWeight;
    private String bubbleWeight;
    private String cargoSize;
    private String quickNumber;
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
    @Generated(hash = 184852901)
    public OldBean(String actualWeight, String bubbleWeight, String cargoSize,
            String quickNumber, String mTypeOfGoods, String mPackingType,
            String mSenderOddNumber, String mSenderTheSender,
            String mSenderPhoneNumber, String mSenderCompany, String mSenderAddress,
            String mCollectionTheSender, String mCollectionPhoneNumber,
            String mCollectionCompany, String mCollectionAddress) {
        this.actualWeight = actualWeight;
        this.bubbleWeight = bubbleWeight;
        this.cargoSize = cargoSize;
        this.quickNumber = quickNumber;
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
    @Generated(hash = 379531948)
    public OldBean() {
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


}
