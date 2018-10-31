package com.spd.pk30dome.database;


import java.util.List;

/**
 * @author :Reginer in  2018/7/4 16:00.
 * 联系方式:QQ:282921012
 * 功能描述:数据库操作
 */
public class DaoOptions {
    /**
     * 保存一些数据到greendao数据库(多条数据)===quick相关===
     * q
     *
     * @param userEntities 用户数据
     */
    public static void saveQuickBeanData(List<QuickBean> userEntities) {
        DaoManager.getInstance().getDao().getQuickBeanDao().insertOrReplaceInTx(userEntities);
    }

    public static List<QuickBean> queryQuickBean(String workNo) {
        return DaoManager.getInstance().getDao().getQuickBeanDao().queryBuilder().where(QuickBeanDao.Properties.MSenderOddNumber.eq(workNo)).list();
    }

    /**
     * 保存配置单条信息到greendao数据库（单条数据）
     * q
     *
     * @param configEntity 用户数据y
     */
    public static void saveQuickDataBean(QuickDataBean configEntity) {
        DaoManager.getInstance().getDao().getQuickDataBeanDao().insertOrReplaceInTx(configEntity);
    }

    public static QuickDataBean queryQuickDataBean(String workNo) {
        return DaoManager.getInstance().getDao().getQuickDataBeanDao().queryBuilder().where(QuickDataBeanDao.Properties.MSenderOddNumber.eq(workNo)).unique();
    }


    /**
     * 保存一些数据到greendao数据库(多条数据)===heavy相关===
     * q
     *
     * @param userEntities 用户数据
     */
    public static void saveHeavyBeanData(List<HeavyBean> userEntities) {
        DaoManager.getInstance().getDao().getHeavyBeanDao().insertOrReplaceInTx(userEntities);
    }

    public static List<HeavyBean> queryHeavyBean(String workNo) {
        return DaoManager.getInstance().getDao().getHeavyBeanDao().queryBuilder().where(HeavyBeanDao.Properties.MSenderOddNumber.eq(workNo)).list();
    }

    /**
     * 保存配置单条信息到greendao数据库（单条数据）
     * q
     *
     * @param configEntity 用户数据y
     */
    public static void saveHeavyDataBean(HeavyDataBean configEntity) {
        DaoManager.getInstance().getDao().getHeavyDataBeanDao().insertOrReplaceInTx(configEntity);
    }

    public static HeavyDataBean queryHeavyDataBean(String workNo) {
        return DaoManager.getInstance().getDao().getHeavyDataBeanDao().queryBuilder().where(HeavyDataBeanDao.Properties.MSenderOddNumber.eq(workNo)).unique();
    }

}
