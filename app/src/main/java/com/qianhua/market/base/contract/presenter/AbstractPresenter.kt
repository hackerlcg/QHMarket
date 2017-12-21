package com.qianhua.market.base.contract.presenter

import com.qianhua.market.base.contract.BaseContract


/**
 * 初始化mView
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/11/8
 */
abstract class AbstractPresenter<V : BaseContract.BaseView>(v : V) {
    var mView = v
    protected set
}