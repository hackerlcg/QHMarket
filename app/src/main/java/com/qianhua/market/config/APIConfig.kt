package com.qianhua.market.config

import com.qianhua.market.BuildConfig


/**
 * 提供API接口地址，以及其他接口配置参数、方法
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/11/7
 */
class APIConfig {
    companion object {
        const val BASE_URL = BuildConfig.BASE_URL
        const val BASE_PATH = "/s1"
        const val PRODUCT_PATH = "/s3"
        const val SECRET_KEY = "0bca3e8e2baa42218040c5dbf6978f315e104e5c"
        const val ACCESS_KEY = "699b9305418757ef9a26e5a32ca9dbfb"
    }
}