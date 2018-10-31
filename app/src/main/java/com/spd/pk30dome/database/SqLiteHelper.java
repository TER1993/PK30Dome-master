package com.spd.pk30dome.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.spd.pk30dome.bean.DaoMaster;


/**
 * @author :Reginer in  2018/7/4 16:01.
 *         联系方式:QQ:282921012
 *         功能描述:
 */
public class SqLiteHelper extends DaoMaster.OpenHelper {
    SqLiteHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
