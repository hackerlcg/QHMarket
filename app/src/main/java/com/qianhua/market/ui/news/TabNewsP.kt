package com.qianhua.market.ui.news

import com.qianhua.market.base.contract.presenter.PresenterDataWrapper
import com.qianhua.market.entity.News
import com.qianhua.market.net.api.ApiManager
import com.qianhua.market.net.helper.SchedulersDataHelper
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper

/**
 * 新闻列表逻辑
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/8
 */
class TabNewsP(v: TabNewsC.View):
        PresenterDataWrapper<MutableList<News.Row>, TabNewsC.View>(v),
        TabNewsC.Presenter<MutableList<News.Row>> {
    private val PAGE_SIZE = 10
    private var curPage = 1
    //是否已经没有更多历史可加载
    private var reachEnd: Boolean = false
    init {
        mData = ArrayList()
    }

    override fun loadMore() {
        fetchNews()
    }

    override fun fetch() {
        if (mData.size > 0){
            mView.updateUI()
            if (reachEnd){
                mView.showNoMoreNews()
            }
        } else {
            refreshData()
        }
    }

    private fun fetchNews(){
        ApiManager.mainApi.fetchNews(curPage, PAGE_SIZE)
                .compose(mView.getLifecycleDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersDataHelper.handleResult())
                .subscribe({ processData(it) },
                        { errorData(it) })
    }

    private fun processData(d: News){
        if (d.total <= 0 && curPage ==1){
            mView.showNoNews()
        } else if (d.total <= 0 && curPage >1){
            mView.showNoMoreNews()
        } else {
            if (curPage == 1) mData.clear()
            mData.addAll(d.rows!!)
            mView.updateUI()
            curPage++
        }
    }

    override fun refreshData() {
        curPage = 1
        fetchNews()
    }

    override fun getData(): MutableList<News.Row> {
        return mData
    }

    override fun close() {
    }

}