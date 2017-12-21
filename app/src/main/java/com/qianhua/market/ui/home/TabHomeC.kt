package com.qianhua.market.ui.home

import com.qianhua.market.base.contract.BaseContract
import com.qianhua.market.entity.AdBanner
import com.qianhua.market.entity.HotNews
import com.qianhua.market.entity.LoanProduct
import com.qianhua.market.entity.NoticeAbstract

/**
 * 首页接口
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/6
 */
interface TabHomeC {
    interface Presenter<out T> : BaseContract.BasePresenter<T>{
        fun checkMsg()

        fun checkMyWorth()

        fun getLoanData(): MutableList<LoanProduct.Row>
    }

    interface View : BaseContract.BaseView{
        fun showBanner(list: MutableList<AdBanner>)

        fun showBorrowingScroll(list: MutableList<String>)

        fun showHotNews(news: MutableList<HotNews>)

        fun showHotLoanProducts(products: MutableList<LoanProduct.Row>)

        fun showAdDialog(ad: AdBanner)

        fun showNotice(notice: NoticeAbstract)

        fun showError()

        fun navigateLogin()

        fun navigateWorthTest()

        fun navigateMessageCenter()
    }
}
