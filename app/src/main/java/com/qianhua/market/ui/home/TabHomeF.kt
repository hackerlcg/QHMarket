package com.qianhua.market.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputFilter
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qianhua.market.R
import com.qianhua.market.base.fragment.BasePTabFragment
import com.qianhua.market.base.glide.GlideA
import com.qianhua.market.config.Constant
import com.qianhua.market.entity.AdBanner
import com.qianhua.market.entity.HotNews
import com.qianhua.market.entity.LoanProduct
import com.qianhua.market.entity.NoticeAbstract
import com.qianhua.market.entity.busevents.NavigateLoan
import com.qianhua.market.entity.busevents.NavigateNews
import com.qianhua.market.ui.home.adapter.HotNewsAdapter
import com.qianhua.market.ui.adapter.LoanRVAdapter
import com.qianhua.market.ui.main.MainActivity
import com.qianhua.market.utils.*
import com.qianhua.market.view.AutoTextView
import com.qianhua.market.view.StateLayout
import com.qianhua.market.view.dialog.AdDialog
import com.qianhua.market.view.rvdecoration.HomeItemDeco
import com.qianhua.market.view.rvdecoration.HotNewsItemDeco
import com.qianhua.market.view.stateprovider.HomeStateViewProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_tab_home.*
import kotlinx.android.synthetic.main.layout_tab_home_rv_header.*
import kotlinx.android.synthetic.main.layout_tab_home_rv_header.view.*
import org.greenrobot.eventbus.EventBus

/**
 * 首页view
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/6
 */
class TabHomeF: BasePTabFragment<TabHomeC.Presenter<MutableList<AdBanner>>>(R.layout.fragment_tab_home),
        TabHomeC.View,View.OnClickListener {

    private lateinit var loanAdapter : LoanRVAdapter

    //记录顶部banner的高度
    private var bannerHeight: Int = 0
    //status and tool bar render
    var toolBarBgAlpha: Float = 0.toFloat()
    private var headHolder : HeaderViewHolder? = null

    //记录输入的钱数
    private var inputMoney = Constant.DEFAULT_FILTER_MONEY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        if(headHolder != null){
            headHolder!!.view.banner.startAutoPlay()
        }
    }

    override fun onPause() {
        super.onPause()
        headHolder!!.view.banner.stopAutoPlay()
    }

    override fun onDestroyView() {
        headHolder?.destroy()
        headHolder = null
        super.onDestroyView()
    }

    internal fun renderStatusAndToolBar(alpha: Float) {
        toolBarBgAlpha = alpha
        var alphaInt = (alpha * 255).toInt()
        alphaInt = if (alphaInt < 10) 0 else alphaInt
        alphaInt = if (alphaInt > 255) 255 else alphaInt
        tool_bar_container.setBackgroundColor(Color.argb(alphaInt, 85, 145, 255))
        center_text.setTextColor(Color.argb(alphaInt, 255, 255, 255))
    }

    override fun updateUI() {

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.worth_test -> {
                if (!FastClickUtils.isFastClick()) {
                    mPresenter.checkMyWorth()
                }
            }
            R.id.ly_etbg -> InputMethodUtil.openSoftKeyboard(activity, et_money)
            R.id.tv_tuijian -> {
                val moneyStr = et_money.text.toString()
                if (TextUtils.isEmpty(moneyStr)) {
                    ToastUtils.showShort(context, "请输入金额", null)
                    return
                }
                try {
                    inputMoney = Integer.parseInt(moneyStr)
                    if (inputMoney < Constant.MIN_FILTER_MONEY) {
                        ToastUtils.showShort(context, "最低借款${Constant.MIN_FILTER_MONEY}元", null)
                        return
                    }
                    EventBus.getDefault().post(NavigateLoan(inputMoney))
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    ToastUtils.showShort(context, "请输入正确金额", null)
                }

            }
            R.id.tv_more -> {
                //umeng统计
//                Statistic.onEvent(Events.HOME_CLICK_VIEW_MORE)
//
                EventBus.getDefault().post(NavigateNews())
            }
        }
    }

    override fun showToast(str: String) {
        if (refresh_layout.isRefreshing){
            refresh_layout.isRefreshing = false
        }
        ToastUtils.showShort(context, str, null)
    }

    override fun showBanner(list: MutableList<AdBanner>) {
        handleShowContent()
        headHolder?.loadBanner(list)
    }

    private fun handleShowContent() {
        if (state_layout != null) {
            state_layout.switchState(StateLayout.STATE_CONTENT)
        }
        if (refresh_layout != null && refresh_layout.isRefreshing) {
            refresh_layout.isRefreshing = false
        }
    }

    override fun onDialog(show: Boolean) {
    }

    override fun showBorrowingScroll(list: MutableList<String>) {
        handleShowContent()
        headHolder!!.view.marqueeView?.startWithList(list)
    }

    override fun showHotNews(news: MutableList<HotNews>) {
        handleShowContent()
        headHolder!!.newsAdapter.notifyHotNewsChanged(news)
    }

    override fun <R> getLifecycle2(): LifecycleTransformer<R> {
        return bindToLifecycle()
    }

    override fun showHotLoanProducts(products: MutableList<LoanProduct.Row>) {
        handleShowContent()
        if (loanAdapter != null) {
            loanAdapter.notifyDataSetChanged()
//            loanAdapter.notifyLoanProductChanged(products)
        }
    }

    override fun <R> getLifecycleDestroy(): LifecycleTransformer<R> {
        return bindToLifecycleDestroy()
    }

    override fun showAdDialog(ad: AdBanner) {
        AdDialog().setAd(ad).show(childFragmentManager, AdDialog::class.java.simpleName)
    }

    override fun showNotice(notice: NoticeAbstract) {
        notice_container.visibility = View.VISIBLE
        notice_text.setScrollMode(AutoTextView.SCROLL_FAST)
        notice_text.text = notice.title
        notice_container.setOnClickListener({
//            val intent = Intent(context, NoticeDetailActivity::class.java)
//            intent.putExtra("noticeId", notice.getId())
//            startActivity(intent)

            notice_container.visibility = View.GONE

            SPUtils.setNoticeClosed(context, true)
        })
    }

    override fun initUI() {
        state_layout.setStateViewProvider(HomeStateViewProvider({
            mPresenter.fetch()
        }))
        notice_close.setOnClickListener({
            notice_container.visibility = View.GONE
            SPUtils.setNoticeClosed(context, true)
        })
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        loanAdapter = LoanRVAdapter(mPresenter.getLoanData())
        loanAdapter.setOnItemChildClickListener({
            baseQuickAdapter: BaseQuickAdapter<Any, BaseViewHolder>, view: View, i: Int ->
            //            val intent = Intent(activity, LoanDetailActivity::class.java)
//            intent.putExtra("loan", adapter.getItem(position) as LoanProduct.Row)
//            startActivity(intent)
        })

        recycle_view.layoutManager = LinearLayoutManager(context)
        recycle_view.addOnScrollListener(onScrollListener)
        recycle_view.adapter = loanAdapter
        recycle_view.addItemDecoration(HomeItemDeco())

        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val headerView = inflater.inflate(R.layout.layout_tab_home_rv_header, recycle_view, false)
        headHolder = HeaderViewHolder(headerView)
        headHolder!!.view.worth_test.setOnClickListener(this)
        headHolder!!.view.ly_etbg.setOnClickListener(this)
        headHolder!!.view.tv_tuijian.setOnClickListener(this)
        headHolder!!.view.tv_more.setOnClickListener(this)
        headHolder!!.view.et_money.setText(inputMoney.toString())

        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            if (dstart == 0 && !TextUtils.isEmpty(source)) {
                if (source == "0") {
                    return@InputFilter ""
                }
            }
            null
        }
        var filters: Array<InputFilter?>? = headHolder!!.view.et_money.filters
        if (filters == null) {
            filters = arrayOf(filter)
        } else {
            val temp = filters

            filters = arrayOfNulls(temp.size + 1)
            for (i in temp.indices) {
                filters[i] = temp[i]
            }
            filters[temp.size] = filter
        }
        headHolder!!.view.et_money.filters = filters

        loanAdapter.setHeaderView(headerView)

        var statusHeight = CommonUtils.getStatusBarHeight(activity)
        var lp = faked_bar.layoutParams
        lp.height = statusHeight
        faked_bar.layoutParams = lp
        renderStatusAndToolBar(toolBarBgAlpha)

        refresh_layout.setColorSchemeResources(R.color.colorPrimary)
        refresh_layout.setOnRefreshListener { mPresenter.refreshData() }
    }

    override fun showError() {
        if (refresh_layout.isRefreshing) {
            refresh_layout.isRefreshing = false
        }
        state_layout.switchState(StateLayout.STATE_NET_ERROR)
    }

    override fun navigateLogin() {
//        UserAuthorizationActivity.launch(activity, null)
    }

    override fun navigateWorthTest() {
//        val intent = Intent(context, WorthTestActivity::class.java)
//        startActivity(intent)
    }

    override fun attachPresenter() {
        mPresenter = TabHomeP(this,context)
    }

    override fun navigateMessageCenter() {
//        val intent = Intent(context, MessageCenterActivity::class.java)
//        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_tab_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (!FastClickUtils.isFastClick()){
            mPresenter.checkMsg()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initData() {
        refresh_layout.isRefreshing = true
        mPresenter.fetch()
    }

    internal inner class HeaderViewHolder(itemView: View) {

        var newsAdapter: HotNewsAdapter
        val view : View = itemView

        private val bannerAds: MutableList<AdBanner> = ArrayList()

        init {
            view.banner.setDelayTime(5000)
            view.banner.setIndicatorGravity(BannerConfig.RIGHT)
            view.banner.isAutoPlay(true)

            val llm = LinearLayoutManager(context)
            llm.orientation = LinearLayoutManager.HORIZONTAL
            newsAdapter = HotNewsAdapter()
            newsAdapter.setOnItemClickListener({
                baseQuickAdapter: BaseQuickAdapter<Any, BaseViewHolder>, view: View, i: Int ->
                val intent = Intent(context, MainActivity::class.java)
//                intent.putExtra("hotNews", adapter.getData().get(i) as HotNews)
                startActivity(intent)
            })

            view.recycle_hor.layoutManager = llm
            view.recycle_hor.addItemDecoration(HotNewsItemDeco())
            view.recycle_hor.adapter = newsAdapter

            val params = view.ly_banner.layoutParams as LinearLayout.LayoutParams
            params.width = CommonUtils.getScreenMaxWidth(activity, 0)
            bannerHeight = params.width * 530 / 975
            params.height = bannerHeight

            view.ly_banner.layoutParams = params

            view.banner.setOnBannerListener({
                i: Int ->
                val ad = bannerAds[i]
                //统计点击
//                DataStatisticsHelper.getInstance().onAdClicked(ad.getId(), ad.getType())
                //跳原生还是跳Web
                if (ad.isNative) {
//                    val intent = Intent(context, LoanDetailActivity::class.java)
//                    intent.putExtra("loanId", ad.getLocalId())
//                    startActivity(intent)
                } else {
//                    val intent = Intent(context, ComWebViewActivity::class.java)
//                    intent.putExtra("url", ad.getUrl())
//                    intent.putExtra("title", ad.getTitle())
//                    startActivity(intent)
                }
            })
        }

        fun destroy() {
            view.marqueeView.stopFlipping()
        }

        fun loadBanner(list: List<AdBanner>?) {
            if (list != null) {
                bannerAds.addAll(list)
                val images = ArrayList<String>()
                for (i in list.indices) {
                    images.add(list[i].imgUrl!!)
                }
                view.banner.setImages(images).setImageLoader(BannerImageLoader()).start()
            }
        }
    }

    private inner class BannerImageLoader : ImageLoader() {

        override fun displayImage(context: Context, path: Any, imageView: ImageView) {
            GlideA.with(context)
                    .load(path as String)
                    .centerCrop()
                    .placeholder(R.drawable.banner_place_holder)
                    .into(imageView)
        }
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        internal var scrollY: Int = 0

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            scrollY += dy
            var maxMove = bannerHeight / 2
            renderStatusAndToolBar(scrollY / maxMove.toFloat())
        }

    }

}