package phy.l1.dao.annotation

/**
 * Created by phy on 2018/3/8.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class DbField(val value: String, val isNotNull: Boolean)