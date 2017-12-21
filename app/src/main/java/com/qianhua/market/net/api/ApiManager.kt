package com.qianhua.market.net.api

import com.qianhua.market.net.NetManager


/**
 * API管理并提供接口引用
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/11/8
 */
object ApiManager {
    val mainApi: MainApi = NetManager.mRetrofit.create(MainApi::class.java)
}