package com.qianhua.market.ui.loanlist

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qianhua.market.R
import com.qianhua.market.base.fragment.BasePTabFragment
import com.qianhua.market.entity.LoanProduct
import com.qianhua.market.ui.adapter.LoanRVAdapter
import com.qianhua.market.utils.ToastUtils
import com.qianhua.market.view.StateLayout
import com.qianhua.market.view.dialog.MoneyFilterPopup
import com.qianhua.market.view.dialog.ProFilterPopup
import com.qianhua.market.view.dialog.TimeFilterPopup
import com.qianhua.market.view.drawable.BlurDrawable
import com.qianhua.market.view.rvdecoration.LoanItemDeco
import com.qianhua.market.view.stateprovider.ProductStateProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import kotlinx.android.synthetic.main.fragment_tab_loan.*

/**
 * 新闻列表view
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/7
 */
class TabLoanF:
        BasePTabFragment<TabLoanC.Presenter<MutableList<LoanProduct.Row>>>(R.layout.fragment_tab_loan),
        TabLoanC.View, MoneyFilterPopup.onBrMoneyListener, TimeFilterPopup.onBrTimeListener,
        ProFilterPopup.onBrZhiyeListener {

    private var pendingAmount = -1

    private lateinit var loanRVAdapter: LoanRVAdapter

    override fun initUI() {
        state_layout.setStateViewProvider(ProductStateProvider(context, {
            mPresenter.refreshData()
        }))
        blur_view.background = BlurDrawable(context, loan_container)
        loanRVAdapter = LoanRVAdapter(mPresenter.getData())
        loanRVAdapter.setOnItemChildClickListener({
            adapter: BaseQuickAdapter<Any, BaseViewHolder>, view: View, i: Int ->
            var loan = adapter.getItem(i) as LoanProduct.Row
//            val intent = Intent(activity, LoanDetailActivity::class.java)
//            intent.putExtra("loan", loan)
//            startActivity(intent)
//            if (loan != null) {
//                //umeng统计
//                Statistic.onEvent(Events.ENTER_LOAN_DETAIL_PAGE, loan.getId())
//            }
        })
        loanRVAdapter.setOnLoadMoreListener({
            mPresenter.loadMore()
        }, recycle_view)

        recycle_view.layoutManager = LinearLayoutManager(context)
        recycle_view.adapter = loanRVAdapter
        recycle_view.addItemDecoration(LoanItemDeco())

        refresh_layout.setColorSchemeResources(R.color.colorPrimary)
        refresh_layout.setOnRefreshListener { mPresenter.refreshData() }

        money_filter.setOnClickListener(this)
        time_filter.setOnClickListener(this)
        pro_filter.setOnClickListener(this)
    }

    override fun attachPresenter() {
        mPresenter = TabLoanP(this)
    }

    override fun initData() {
        refresh_layout.isRefreshing = true
        mPresenter.fetch()
        if (pendingAmount != -1){
            onMoneyItemClick(pendingAmount)
            pendingAmount = -1
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.money_filter -> {
//                //umeng统计
//                Statistic.onEvent(Events.LOAN_CLICK_AMOUNT_FILTER)
                var moneyFP = MoneyFilterPopup(activity,mPresenter.getFilterAmount(), blur_view,
                        money_filter_text, money_filter_image)
                moneyFP.setShareItemListener(this)
                moneyFP.showAsDropDown(filter_container)
            }
            R.id.time_filter -> {
//                //umeng统计
//                Statistic.onEvent(Events.LOAN_CLICK_TIME_FILTER)

                var timeFP = TimeFilterPopup(activity, mPresenter.getFilterDueTimeSelected(),
                        blur_view, time_filter_text, time_filter_image, mPresenter.getFilterDueTime())
                timeFP.setShareItemListener(this)
                timeFP.showAsDropDown(filter_container)
            }
            R.id.pro_filter -> {
                //umeng统计
//                Statistic.onEvent(Events.LOAN_CLICK_PRO_FILTER)

                var proFP = ProFilterPopup(activity,blur_view, pro_filter_text, pro_filter_image)
                proFP.setShareItemListener(this)
                proFP.showAsDropDown(filter_container)
            }
        }
    }

    override fun updateUI() {

    }

    fun setQueryMoney(queryMoney: Int) {
        this.pendingAmount = queryMoney
    }

    override fun showToast(str: String) {
        ToastUtils.showShort(context, str, null)
    }

    override fun onDialog(show: Boolean) {

    }

    override fun showFilters(amount: String, dueTime: String, pro: String) {
        money_filter_content.text = "${amount}元"
        time_filter_content.text = dueTime
        pro_filter_content.text = pro
    }

    override fun <R> getLifecycle2(): LifecycleTransformer<R> {
        return bindToLifecycle()
    }

    override fun showLoanProduct(list: MutableList<LoanProduct.Row>, enableLoadMore: Boolean) {
        if (loanRVAdapter.isLoading){
            loanRVAdapter.loadMoreComplete()
        }
        loanRVAdapter.setEnableLoadMore(enableLoadMore)
        if (refresh_layout.isRefreshing) {
            refresh_layout.isRefreshing = false
        }
        state_layout.switchState(StateLayout.STATE_CONTENT)
        loanRVAdapter.notifyDataSetChanged()
    }

    override fun <R> getLifecycleDestroy(): LifecycleTransformer<R> {
        return  bindToLifecycleDestroy()
    }

    override fun showNoLoanProduct() {
        if (!isAdded){
            return
        }
        if (refresh_layout.isRefreshing){
            refresh_layout.isRefreshing = false
        }
        state_layout.switchState(StateLayout.STATE_EMPTY)
    }

    override fun showNoMoreLoanProduct() {
        loanRVAdapter.setEnableLoadMore(false)
        loanRVAdapter.loadMoreComplete()
    }

    override fun onTimeItemClick(selectTimeIndex: Int) {
        mPresenter.filterDueTime(selectTimeIndex)
    }

    override fun onZhiyeItemClick(selectIndex: Int) {
        mPresenter.filterPro(selectIndex)
    }

    override fun onMoneyItemClick(money: Int) {
        mPresenter.filterAmount(money)
    }
}