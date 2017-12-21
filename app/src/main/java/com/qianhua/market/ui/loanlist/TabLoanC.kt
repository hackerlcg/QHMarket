package com.qianhua.market.ui.loanlist

import com.qianhua.market.base.contract.BaseContract
import com.qianhua.market.entity.LoanProduct

/**
 * 贷款列表页接口
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/7
 */
interface TabLoanC {
    interface Presenter<T>: BaseContract.BasePresenter<T> {
        fun filterAmount(amount: Int)

        fun filterDueTime(selected: Int)

        fun filterPro(selected: Int)

        fun loadMore()

        fun getFilterAmount(): String

        fun getFilterDueTime(): Array<String>

        fun getFilterDueTimeSelected(): Int

        fun getFilterPro(): Array<String>
    }

    interface View: BaseContract.BaseView {
        fun showFilters(amount: String, dueTime: String, pro: String)

        fun showLoanProduct(list: MutableList<LoanProduct.Row>, enableLoadMore: Boolean)

        fun showNoLoanProduct()

        fun showNoMoreLoanProduct()
    }
}