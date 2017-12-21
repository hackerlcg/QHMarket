package com.qianhua.market.ui.news

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qianhua.market.R
import com.qianhua.market.base.fragment.BasePTabFragment
import com.qianhua.market.entity.News
import com.qianhua.market.ui.news.adapter.NewsAdapter
import com.qianhua.market.utils.ToastUtils
import com.qianhua.market.view.StateLayout
import com.qianhua.market.view.rvdecoration.NewsItemDeco
import com.qianhua.market.view.stateprovider.NewsStateViewProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import kotlinx.android.synthetic.main.fragment_tab_news.*

/**
 * 新闻列表view
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/8
 */
class TabNewsF: BasePTabFragment<TabNewsC.Presenter<MutableList<News.Row>>>(R.layout.fragment_tab_news), TabNewsC.View {
    private lateinit var adapter: NewsAdapter

    override fun onClick(v: View) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun attachPresenter() {
        mPresenter = TabNewsP(this)
    }

    override fun initUI() {
        state_layout.setStateViewProvider(NewsStateViewProvider(context,{
            mPresenter.refreshData()
            adapter.loadMoreEnd(false)
        }))
        adapter = NewsAdapter(mPresenter.getData(),context)
        adapter.setOnItemChildClickListener {
            baseQuickAdapter: BaseQuickAdapter<Any, BaseViewHolder>, view: View, position: Int ->
            val news = adapter.data[position] as News.Row
//            val intent = Intent(activity, NewsDetailActivity::class.java)
//            intent.putExtra("news", news)
//            startActivity(intent)
//
//            if (news != null) {
//                //Umeng统计
//                Statistic.onEvent(Events.ENTER_NEWS_DETAIL, news.getId())
//            }
        }
        adapter.setOnLoadMoreListener({mPresenter.loadMore()}, recycler_view)
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = adapter
        val density = context.resources.displayMetrics.density
        val padding = (density * 8).toInt()
        recycler_view.addItemDecoration(NewsItemDeco((density * 0.5).toInt(), padding, padding))

        refresh_layout.setColorSchemeResources(R.color.colorPrimary)
        refresh_layout.setOnRefreshListener { mPresenter.refreshData() }
    }

    override fun initData() {
        refresh_layout.isRefreshing = true
        mPresenter.fetch()
    }

    override fun updateUI() {
        if (!isAdded) return
        state_layout.switchState(StateLayout.STATE_CONTENT)
        recycler_view.setBackgroundColor(Color.WHITE)
        adapter.notifyDataSetChanged()
        if (adapter.isLoading){
            adapter.loadMoreComplete()
        }
        if (refresh_layout.isRefreshing){
            refresh_layout.isRefreshing = false
        }

    }

    override fun showNoNews() {
        if (!isAdded) return
        if (refresh_layout.isRefreshing) refresh_layout.isRefreshing = false
        state_layout.switchState(StateLayout.STATE_EMPTY)
    }

    override fun showNetError() {
        if (!isAdded) return
        if (refresh_layout.isRefreshing) refresh_layout.isRefreshing = false
        state_layout.switchState(StateLayout.STATE_NET_ERROR)
    }

    override fun showNoMoreNews() {
        if (!isAdded) return
        adapter.loadMoreEnd(true)
    }

    override fun showToast(str: String) {
        ToastUtils.showShort(context, str, null)
    }

    override fun onDialog(show: Boolean) {
    }

    override fun <R> getLifecycle2(): LifecycleTransformer<R> {
        return bindToLifecycle()
    }

    override fun <R> getLifecycleDestroy(): LifecycleTransformer<R> {
        return bindToLifecycleDestroy()
    }
}