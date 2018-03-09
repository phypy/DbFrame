package phy.l1.dao.annotation

/**
 * Created by phy on 2018/3/8.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation  class  DbTable(val value:String)