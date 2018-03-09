package phy.l1.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log

/**
 * Created by phy on 2018/3/9.
 */
class BaseDaoFactory {
    var sqliteDataBase: SQLiteDatabase

    companion object {
        var mFactory: BaseDaoFactory? = null
        fun getInstance(context: Context, dbName: String): BaseDaoFactory {
            if (mFactory == null) {
                synchronized(BaseDaoFactory::class.java) {
                    if (mFactory == null) {
                        mFactory = BaseDaoFactory(context, dbName)
                    }
                }
            }
            return mFactory!!
        }
    }

    private constructor(context: Context, dbName: String) {
        sqliteDataBase = context.openOrCreateDatabase(dbName, 0, null)
        var path = context.getDatabasePath(dbName).path
        Log.e("TAG", "DbPath:$path")
    }

    fun <T> getBaseDao(entityClass: Class<T>): BaseDao<T> {
        var baseDao = BaseDao::class.java.newInstance() as BaseDao<T>
        baseDao.init(sqliteDataBase, entityClass)
        return baseDao
    }

}