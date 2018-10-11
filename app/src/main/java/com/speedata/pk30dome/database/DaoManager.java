package com.speedata.pk30dome.database;

import android.app.Application;

import com.speedata.pk30dome.bean.DaoMaster;
import com.speedata.pk30dome.bean.DaoSession;

import org.greenrobot.greendao.database.Database;


/**
 * @author :Reginer in  2018/7/4 16:01.
 * 联系方式:QQ:282921012
 * 功能描述:
 */
public class DaoManager {
    private static DaoSession sDaoSession;

    public static void init(Application application) {
        SqLiteHelper helper = new SqLiteHelper(application, "PK30.db");
        Database db = helper.getWritableDb();
        sDaoSession = new DaoMaster(db).newSession();
    }


    private static class DatabaseManagerHolder {
        private static final DaoManager INSTANCE = new DaoManager();
    }

    public static DaoManager getInstance() {
        return DatabaseManagerHolder.INSTANCE;
    }

    public DaoSession getDao() {
        return sDaoSession;
    }
}
