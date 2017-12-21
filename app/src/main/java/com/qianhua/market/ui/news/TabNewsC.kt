package com.qianhua.market.ui.news

import com.qianhua.market.base.contract.BaseContract

/**
 * news interface
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/8
 */
interface TabNewsC {
    interface Presenter<T>: BaseContract.BasePresenter<T>{
        fun loadMore()
    }

    interface View: BaseContract.BaseView{
        fun showNoNews()

        fun showNetError()

        fun showNoMoreNews()
    }
}