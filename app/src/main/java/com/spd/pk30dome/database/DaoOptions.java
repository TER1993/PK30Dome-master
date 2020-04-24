package com.spd.pk30dome.database;


/**
 * @author :Reginer in  2018/7/4 16:00.
 * 联系方式:QQ:282921012
 * 功能描述:数据库操作
 */
public class DaoOptions {

    /**
     * 保存配置单条信息到greendao数据库（单条数据）
     * q
     *
     * @param oldBean 用户数据y
     */
    public static void saveOldBean(OldBean oldBean) {
        DaoManager.getInstance().getDao().getOldBeanDao().insertOrReplaceInTx(oldBean);
    }

    public static OldBean queryOldBean(String workNo) {
        return DaoManager.getInstance().getDao().getOldBeanDao().queryBuilder().where(OldBeanDao.Properties.MSenderOddNumber.eq(workNo)).unique();
    }

}
