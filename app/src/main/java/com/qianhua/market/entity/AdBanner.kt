package com.qianhua.market.entity


class AdBanner {
    /**
     * 广告类型，1 跳转url， 2跳转产品详情页
     */
    var type: Int = 0
    var url: String? = null
    var localId: String? = null
    var imgUrl: String? = null
    var id: String? = null
    var title: String? = null

    val isNative: Boolean
        get() = type == 2
}
