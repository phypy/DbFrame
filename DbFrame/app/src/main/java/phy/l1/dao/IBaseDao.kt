package phy.l1.dao

/**
 * Created by phy on 2018/3/8.
 */
interface IBaseDao<T> {
    fun insert(entity: T): Long
}