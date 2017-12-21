package com.qianhua.market.base.fragment

import com.qianhua.market.base.contract.BaseContract

/**
 * MVP Fragment
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/6
 */
abstract class BasePTabFragment<P: BaseContract.BasePresenter<*>>(id: Int): BaseTabFragment(id) {
    protected lateinit var mPresenter : P
}