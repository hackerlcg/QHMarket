package com.qianhua.market.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.gyf.barlibrary.ImmersionBar
import com.qianhua.market.R
import com.qianhua.market.base.activity.BaseActivity
import com.qianhua.market.entity.busevents.NavigateLoan
import com.qianhua.market.entity.busevents.NavigateNews
import com.qianhua.market.ui.home.TabHomeF
import com.qianhua.market.ui.loanlist.TabLoanF
import com.qianhua.market.ui.mine.TabMineF
import com.qianhua.market.ui.news.TabNewsF
import com.qianhua.market.utils.FastClickUtils
import com.qianhua.market.utils.SPUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList

/**
 * 主要功能模块显示控制
 * 控制主要功能模块点击后不同的显示
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/5
 */
class MainActivity : BaseActivity(R.layout.activity_main){

    private var mSelectedFragmentId = -1

    /**
     * param need passed to target fragment if navigating to TabLoanFragment.
     */
    private var queryMoney = -1

    private var inputMethodManager: InputMethodManager? = null

//    private val updateHelper = AppUpdateHelper.newInstance()

    @SuppressLint("InlinedApi")
    private val needPermission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (getIntent().getBooleanExtra("home", false)) {
            if (navigation_bar != null) {
                navigation_bar.select(R.id.tab_home)
            }
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun initUI() {
        EventBus.getDefault().register(this)
        ImmersionBar.with(this).fitsSystemWindows(false).statusBarColor(R.color.transparent).init()
        navigation_bar.setOnSelectedChangedListener {
            selectedId: Int ->
            if (selectedId != mSelectedFragmentId) {
                selectTab(selectedId)
            }
        }
        navigation_bar.select(R.id.tab_home)
    }

    private fun selectTab(id: Int) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()

        val oldTag = makeTag(mSelectedFragmentId)
        mSelectedFragmentId = id
        val newTag = makeTag(mSelectedFragmentId)
        val lastSelected = fm.findFragmentByTag(oldTag)
        if (lastSelected != null) {
            ft.hide(lastSelected)
        }
        var newSelected: Fragment? = fm.findFragmentByTag(newTag)
        if (newSelected == null) {
            when (id) {
                R.id.tab_home -> newSelected = TabHomeF()
                R.id.tab_loan -> newSelected = TabLoanF()
                R.id.tab_news -> newSelected = TabNewsF()
                R.id.tab_mine -> newSelected = TabMineF()
            }
            ft.add(R.id.tab_fragment, newSelected, newTag)
        } else {
            ft.show(newSelected)
        }
        if (newSelected != null && newSelected is TabLoanF && queryMoney != -1) {
            newSelected.setQueryMoney(queryMoney)
        }
        ft.commit()
    }

    private fun makeTag(id: Int): String {
        return "TabFragmentId=$id"
    }


    override fun attachPresenter() {
    }

    override fun initData() {
        checkPermission()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!SPUtils.getCheckPermission(this)) {
                val permission = ArrayList<String>()
                for (i in needPermission.indices) {
                    if (ContextCompat.checkSelfPermission(this, needPermission[i]) != PackageManager.PERMISSION_GRANTED) {
                        permission.add(needPermission[i])
                    }
                }
                if (permission.size > 0) {
                    try {
                        ActivityCompat.requestPermissions(this, permission.toTypedArray(), 1)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                SPUtils.setCheckPermission(this, true)
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (mSelectedFragmentId == R.id.tab_home) {
            if (ev.action == MotionEvent.ACTION_DOWN) {
                if (inputMethodManager == null) {
                    inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                }
                if (currentFocus != null && currentFocus.windowToken != null) {
                    if (currentFocus is EditText) {
                        currentFocus.clearFocus()
                        inputMethodManager!!.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        if (FastClickUtils.isFastBackPress()) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 点击给我推荐，把条件带过去在第二个页面筛选
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun navigateLoan(event: NavigateLoan) {
        if (event.queryMoney !== -1) {
            queryMoney = event.queryMoney
        }
        navigation_bar.select(R.id.tab_loan)
        queryMoney = -1
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun navigateNews(event: NavigateNews) {
        navigation_bar.select(R.id.tab_news)
    }

}
