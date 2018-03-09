package phy.l1.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import android.util.Log
import phy.l1.dao.annotation.DbField
import phy.l1.dao.annotation.DbTable
import java.lang.reflect.Field
import java.util.*


/**
 * Created by phy on 2018/3/8.
 */
class BaseDao<T> : IBaseDao<T> {
    private lateinit var mSqliteDatabase: SQLiteDatabase
    private lateinit var entityClass: Class<T>
    private lateinit var tableName: String
    private lateinit var cacheMap: HashMap<Field, String>


    override fun insert(entity: T): Long {
        var values = getValue(entity)
        return mSqliteDatabase.insert(tableName, null, values)
    }

    fun init(sqliteDatabase: SQLiteDatabase, entityClass: Class<T>) {
        this.mSqliteDatabase = sqliteDatabase
        this.entityClass = entityClass
        if (!mSqliteDatabase.isOpen) {
            return
        }
        when (entityClass.getAnnotation(DbTable::class.java) == null) {
            true -> tableName = entityClass.simpleName
            false -> {
                tableName = entityClass.getAnnotation(DbTable::class.java).value
                if (TextUtils.isEmpty(tableName)) {
                    throw Exception("Error:annotation should not empty.")
                }
            }
        }
        var cmd = getCreateTableSql()
        Log.e("TAG", "createTableSql:" + cmd)
        mSqliteDatabase.execSQL(cmd)
        initCacheMap()
    }

    private fun getCreateTableSql(): String {
        var sql = StringBuffer()
        sql.append("create table if not exists $tableName (")
        for (field in entityClass.declaredFields) {
            field.isAccessible = true

            if (field.getAnnotation(DbField::class.java) == null) {
                sql.append(field.name)
            } else {
                if (TextUtils.isEmpty(field.getAnnotation(DbField::class.java).value)) {
                    throw Exception("Error:annotation should not empty.")
                }
                sql.append(field.getAnnotation(DbField::class.java).value)
            }
            var type = field.type
            when (type) {
                String::class.java,Date::class.java -> sql.append(" text ")
                Char::class.java, Int::class.java -> sql.append(" integer ")
                Long::class.java -> sql.append(" bigint ")
                Float::class.java, Double::class.java -> sql.append(" real ")
                else -> sql.append(" blob ")
            }
            if (field.getAnnotation(DbField::class.java) != null) {
                if (field.getAnnotation(DbField::class.java).value == "_id") {
                    sql.append(" primary key ")
                }
                if (field.getAnnotation(DbField::class.java).isNotNull) {
                    sql.append(" not null ")
                }
            }
            sql.append(",")
        }
        if (sql[(sql.length - 1)] == ',') {
            sql.deleteCharAt(sql.length - 1)
        }
        sql.append(")")
        return sql.toString()
    }

    private fun initCacheMap() {
        var sql = "select * from $tableName limit 1,0"
        var cur = mSqliteDatabase.rawQuery(sql, null)
        var columnNames = cur.columnNames
        cacheMap = HashMap(columnNames.size)
        var fields = entityClass.declaredFields
        for (field in fields) {
            field.isAccessible = true
        }
        for (column in columnNames) {
            for (field in fields) {
                var fieldName: String
                when (field.getAnnotation(DbField::class.java) != null) {
                    true -> fieldName = field.getAnnotation(DbField::class.java).value
                    false -> fieldName = field.name
                }
                if (column == fieldName) {
                    cacheMap.put(field, column)
                    break
                }
            }
        }
        cur.close()
    }

    private fun getValue(entity: T): ContentValues {
        var cv = ContentValues()
        var it = cacheMap.keys.iterator()
        while (it.hasNext()) {
            var field = it.next()
            var entityValue = field.get(entity)
            if (entityValue != null) {
                var value = entityValue.toString()
                var key = cacheMap[field]
                if (!TextUtils.isEmpty(key)) {
                    cv.put(key, value)
                }
            }
        }
        return cv
    }
}