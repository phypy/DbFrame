package phy.l1.bean

import android.graphics.Bitmap
import phy.l1.dao.annotation.DbField
import phy.l1.dao.annotation.DbTable
import java.util.*

/**
 * Created by phy on 2017/9/11.
 */
@DbTable("WP_User")
class UserInfo {
    @DbField("_id", true)
    var userId: String? = null
    var profileId: String? = null
    var displayName: String? = null
    var displayImg: String? = null//path
    var displayImgUrl: String? = null
    var bindedDeviceAddr: String? = null
    var bindedDeviceId: String? = null
    var bindStartIndex: Long = 0

    var height = -1
    var weight = -1f
    var birthday: Date? = null
    var menstrualCycle = -1//（如果是 -1 的话需要在初始化 MenstrualRecord 的时候检查这个值）
    var menstrualLast = -1
    var menstrualStartDate: Date? = null

    //do not write into db
    var headImg: Bitmap? = null

    constructor(userId: String) {
        this.userId = userId
    }

}
