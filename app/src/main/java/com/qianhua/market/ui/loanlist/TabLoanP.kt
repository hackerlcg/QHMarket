package com.qianhua.market.ui.loanlist

import com.qianhua.market.base.contract.presenter.PresenterDataWrapper
import com.qianhua.market.config.Constant
import com.qianhua.market.entity.LoanProduct
import com.qianhua.market.net.api.ApiManager
import com.qianhua.market.net.helper.SchedulersDataHelper
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper
import io.reactivex.disposables.Disposable

/**
 * 贷款列表逻辑
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/7
 */
class TabLoanP(v: TabLoanC.View):
        PresenterDataWrapper<MutableList<LoanProduct.Row>, TabLoanC.View>(v),
        TabLoanC.Presenter<MutableList<LoanProduct.Row>>{

    companion object {
        private val PAGE_SIZE = 10
    }

    private val dueTimes = arrayOf("1个月及以下", "3个月", "6个月", "12个月", "24个月", "36个月及以上", "不限")
    private val pros  = arrayOf("上班族", "自由职业", "个体户", "不限")
    private var amount = Constant.DEFAULT_FILTER_MONEY
    private var duTimeSelected = 6
    private var proSelected = 3
    private var curPage = 1

    private var curTask: Disposable? = null

    init {
        mData = ArrayList()
    }

    override fun filterAmount(amount: Int) {
        if (amount != this.amount)  {
            this.amount = amount
            mView.showFilters(amount.toString(), dueTimes[duTimeSelected], pros[proSelected])
            dataClearShow()
        }
    }

    override fun filterDueTime(selected: Int) {
        if (selected != duTimeSelected) {
            duTimeSelected = selected
            mView.showFilters(amount.toString(), dueTimes[duTimeSelected], pros[proSelected])
            dataClearShow()
        }
    }

    override fun filterPro(selected: Int) {
        if (selected != duTimeSelected) {
            duTimeSelected = selected
            mView.showFilters(amount.toString(), dueTimes[duTimeSelected], pros[proSelected])
            dataClearShow()
        }
    }

    private fun dataClearShow(){
        mData.clear()
        mView.showLoanProduct(mData, false)
        refreshData()
    }

    override fun loadMore() {
        fetchLoanProduct()
    }

    override fun getFilterAmount(): String {
        return amount.toString()
    }

    override fun getFilterDueTime(): Array<String> {
        return dueTimes
    }

    override fun getFilterDueTimeSelected(): Int {
        return duTimeSelected
    }

    override fun getFilterPro(): Array<String> {
        return pros
    }

    override fun fetch() {
        mView.showFilters(amount.toString(), dueTimes[duTimeSelected], pros[proSelected])
        if (mData.isNotEmpty()){
            mView.showLoanProduct(mData, mData.size >= PAGE_SIZE)
        } else {
            refreshData()
        }
    }

    override fun refreshData() {
        curPage = 1
        fetchLoanProduct()
    }

    private fun fetchLoanProduct(){
        cancelCurTask()
        ApiManager.mainApi.fetchLoanProduct(amount.toDouble(), "${duTimeSelected + 1}",
                getProParamBySelected(proSelected).toString(), curPage, PAGE_SIZE)
                .compose(mView.getLifecycleDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersDataHelper.handleResult())
                .subscribe({ processData(it) },
                        {
                            errorData(it)
                            mView.showNoLoanProduct()
                        })
    }

    private fun processData(d: LoanProduct) {
        if(d.total == 0 && curPage == 1){
            mView.showNoLoanProduct()
        } else if (d.total == 0 && curPage > 1){
            mView.showNoMoreLoanProduct()
        } else {
            if (curPage == 1) mData.clear()
            mData.addAll(d.rows!!)
            mView.showLoanProduct(mData, mData.size >= PAGE_SIZE)
            curPage++
        }
    }

    override fun getData(): MutableList<LoanProduct.Row> {
        return mData
    }

    override fun close() {
    }


    private fun getProParamBySelected(selected: Int): Int {
        return when (selected) {
            0 -> 2
            1 -> 4
            2 -> 3
            3 -> 1
            else -> 0
        }
    }

    private fun cancelCurTask() {
        if (curTask != null && !curTask!!.isDisposed) {
            curTask!!.dispose()
        }
    }
}