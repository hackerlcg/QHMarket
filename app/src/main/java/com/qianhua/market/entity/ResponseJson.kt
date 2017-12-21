package com.qianhua.market.entity

import com.google.gson.annotations.SerializedName

/**
 * 返回数据接收
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/11/8
 */
open class ResponseJson<T> {
    @SerializedName("data")
    var data : T? = null

    var msg: String = ""
    var code: Int = 0

    fun isSuccess(): Boolean{
        return code == 1000000
    }

}