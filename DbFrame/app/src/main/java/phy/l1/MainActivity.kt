package phy.l1

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import phy.l1.bean.UserInfo
import phy.l1.dao.BaseDaoFactory

class MainActivity : AppCompatActivity() {
    val TEST_DB_NAME = "phy_L1_DbFrame.db"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testDao()
    }

    fun testDao() {
        var baseDao = BaseDaoFactory.getInstance(this, TEST_DB_NAME).getBaseDao(UserInfo::class.java)
        var userInfo = UserInfo("123")
        userInfo.displayName="phy"
        baseDao.insert(userInfo)
    }
}
