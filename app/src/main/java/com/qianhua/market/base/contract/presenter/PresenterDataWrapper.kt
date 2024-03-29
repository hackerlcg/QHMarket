package com.qianhua.market.base.contract.presenter

import com.qianhua.market.base.contract.BaseContract
import com.qianhua.market.net.helper.ThrowableSuccess4DataNull


/**
 * 如果Presenter需要数据那么就定义mData类型，并且在合适的位置初始化
 * 一般是在继承之后的init{}中分配数据空间例如 init { mData = ArrayList() }
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/11/14
 */
open class PresenterDataWrapper<R : Any,V : BaseContract.BaseView>(v : V) : AbstractPresenter<V>(v) {
    lateinit var mData : R
    protected set
    /**
     * 处理数据
     */
    protected open fun processData(d : R) {}
    /**
     * 数据出错
     */
    protected open fun errorData(error : Throwable) {
        if (error is ThrowableSuccess4DataNull) {
            //暂时不处理
        } else {
            mView?.showToast(error.localizedMessage)
        }
    }
}