package com.qianhua.market.ui.home

import android.content.Context
import com.qianhua.market.base.contract.presenter.PresenterDataWrapper
import com.qianhua.market.config.RequestConstants
import com.qianhua.market.entity.AdBanner
import com.qianhua.market.entity.HotNews
import com.qianhua.market.entity.LoanProduct
import com.qianhua.market.entity.NoticeAbstract
import com.qianhua.market.helper.UserHelper
import com.qianhua.market.net.api.ApiManager
import com.qianhua.market.net.helper.SchedulersDataHelper
import com.qianhua.market.utils.SPUtils
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper
import java.util.*
import kotlin.collections.ArrayList

/**
 * 贷款首页presenter
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/6
 */
class TabHomeP(v: TabHomeC.View, c: Context) :
        PresenterDataWrapper<MutableList<AdBanner>, TabHomeC.View>(v),
        TabHomeC.Presenter<MutableList<AdBanner>> {

    private val mContext = c
    private var hasAdInit = false
    private var notice: NoticeAbstract? = null
    private val notices = ArrayList<String>()
    private val hotNews = ArrayList<HotNews>()
    private val hotLoanProducts = ArrayList<LoanProduct.Row>()

    init {
        mData = ArrayList()
    }

    override fun checkMsg() {
        if (UserHelper.getInstance(mContext).profile != null) {
            mView.navigateMessageCenter()
        } else {
            mView.navigateLogin()
        }
    }

    override fun checkMyWorth() {
        if (UserHelper.getInstance(mContext).profile != null) {
            mView.navigateWorthTest()
        } else {
            mView.navigateLogin()
        }
    }

    override fun fetch() {
        //如果已经过初始化，则直接返回数据
        //ad dialog
        if (!hasAdInit) {
            fetchAd()
        }
        //notice
        if (notice == null) {
            fetchNotice()
        } else if (!SPUtils.getNoticeClosed(mContext)) {
            mView.showNotice(notice!!)
        }
        //banner
        if (mData.size == 0) {
            fetchBanner()
        } else {
            mView.showBanner(Collections.unmodifiableList(mData))
        }
        //loan success notice
        if (notices.size == 0) {
            fetchScrolling()
        } else {
            mView.showBorrowingScroll(notices)
        }
        //news
        if (hotNews.size == 0) {
            fetchHotNews()
        } else {
            mView.showHotNews(hotNews)
        }
        //hot loan products
        if (hotLoanProducts.size == 0) {
            fetchHotLoanProducts()
        } else {
            mView.showHotLoanProducts(hotLoanProducts)
        }
    }

    private fun fetchBanner(){
        ApiManager.mainApi.fetchSupernatant(RequestConstants.PLATFORM, RequestConstants.SUP_TYPE_BANNER)
                .compose(mView.getLifecycleDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersDataHelper.handleResult())
                .subscribe({ processDataBanner(it) },
                        { errorData(it) })
    }

    private fun fetchAd() {
        ApiManager.mainApi.fetchSupernatant(RequestConstants.PLATFORM, RequestConstants.SUP_TYPE_DIALOG)
                .compose(mView.getLifecycleDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersDataHelper.handleResult())
                .subscribe({ processData(it) },
                        { errorData(it) })
    }

    private fun fetchNotice() {
        ApiManager.mainApi.noticeHome()
                .compose(mView.getLifecycleDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersDataHelper.handleResult())
                .subscribe({ processData(it) },
                        { errorData(it) })
    }

    private fun fetchScrolling() {
        ApiManager.mainApi.fetchBorrowingScroll()
                .compose(mView.getLifecycleDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersDataHelper.handleResult())
                .subscribe({ processDataScrolling(it) },
                        { errorData(it) })
    }


    private fun fetchHotNews() {
        ApiManager.mainApi.fetchHotNews()
                .compose(mView.getLifecycleDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersDataHelper.handleResult())
                .subscribe({ processDataNews(it) },
                        { errorData(it) })
    }


    private fun fetchHotLoanProducts() {
        ApiManager.mainApi.fetchHotLoanProducts()
                .compose(mView.getLifecycleDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersDataHelper.handleResult())
                .subscribe({ processDataLoan(it) },
                        { errorData(it) })
    }

    fun processDataBanner(d: MutableList<AdBanner>) {
        if (d.size > 0){
            mData.clear()
            mData.addAll(d)
            mView.showBanner(mData)
        } else {
            mView.showBanner(mData)
        }
    }

    /**
     * loading AD data
     */
    override fun processData(d: MutableList<AdBanner>) {
        super.processData(d)
        hasAdInit = true
        if (d.size > 0) {
            var adBanner = d[0]
            var lastId = SPUtils.getLastDialogAdId(mContext)
            //同一个广告只显示一次
            if (lastId == null || lastId != adBanner.localId) {
                mView.showAdDialog(adBanner)
            }
            //记录已展示的id
            SPUtils.setLastDialogAdId(mContext, adBanner.localId)
        }
    }

    /**
     * loading 公告
     */
    private fun processData(d: NoticeAbstract) {
        notice = d
        if (notice?.id != null) {
            if (!notice?.id.equals(SPUtils.getLastNoticeId(mContext))) {
                mView.showNotice(notice!!)
                SPUtils.setNoticeClosed(mContext, false)
            } else if (!SPUtils.getNoticeClosed(mContext)) {
                mView.showNotice(notice!!)
            }
            SPUtils.setLastNoticeId(mContext, notice?.id)
        }
    }

    /**
     * loading Scrolling
     */
    private fun processDataScrolling(d: MutableList<String>) {
        if (d.size > 0) {
            notices.clear()
            notices.addAll(d)
            mView.showBorrowingScroll(notices)
        }
    }

    private fun processDataNews(d: MutableList<HotNews>) {
        if (d.size > 0) {
            hotNews.clear()
            hotNews.addAll(d)
            mView.showHotNews(hotNews)
        }
    }

    private fun processDataLoan(d: MutableList<LoanProduct.Row>) {
        if (d.size > 0) {
            hotLoanProducts.clear()
            hotLoanProducts.addAll(d)
            mView.showHotLoanProducts(hotLoanProducts)
        }
    }

    override fun refreshData() {
        fetchNotice()
        fetchScrolling()
        fetchHotNews()
        fetchHotLoanProducts()
    }

    override fun getData(): MutableList<AdBanner> {
        return mData
    }

    override fun getLoanData(): MutableList<LoanProduct.Row> {
        return hotLoanProducts
    }

    override fun close() {
    }

}