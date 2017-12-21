package com.qianhua.market.base.activity

import android.os.Build
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.View
import com.gyf.barlibrary.ImmersionBar
import com.qianhua.market.R
import com.qianhua.market.view.dialog.CommNoneAndroidLoading
import gear.yc.com.gearlibrary.manager.ActivityManager

/**
 * app activity基类
 * app内所有类原则上都要继承这个类，这个类中集合了共有的方法、参数、初始化
 * 但要注意初始化的东西不要太多以免影响activity加载
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/11/1
 */
abstract class BaseActivity(id: Int) : RxLifecycleActivity() {
    protected val LAYOUT_RES_ID = id
    protected var loading: CommNoneAndroidLoading? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).init()
        ActivityManager.getInstance().activities.add(this)
        if (savedInstanceState != null) {
            val manager = supportFragmentManager
            manager.popBackStackImmediate(null, 1)
        }
        setContentView(LAYOUT_RES_ID)
        attachPresenter()
        initUI()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ImmersionBar.with(this).destroy()
    }

    /**
     * 主要想法是把初始化view的代码放到一起，所以在此定义这个方法，每个继承类都需要实现此方法
     * 并且这个方法会在initData()之前执行
     * @see initData
     */
    abstract protected fun initUI()

    /**
     * 初始化Presenter
     * @see initUI
     */
    abstract protected fun attachPresenter()

    /**
     * 初始化数据的代码统一存放位置，attachPresenter()之后执行
     * @see attachPresenter
     */
    abstract protected fun initData()

    /**
     * 退出应用可以调用此方法， ActivityManager 会储存所有的activity类并且关闭它们
     * 当然这只是退出应用的一种方式，还可以使用某个singTask启动方式的activity类，
     * 并把它放到栈底，当你要退出应用的时候只需要进入这个activity然后调用finish就可以了
     * 当你使用这种方式关闭，你要注意你的app中是否有singleInstance启动的activity，
     * 因为它并不能正确的让singleInstance启动的activity出栈
     *
     * 如果你不了解activity的启动方式，那么可以去这个app的manifest文件中寻找答案
     */
    fun exitApp() {
        ActivityManager.getInstance().clearAllActivity()
        System.exit(0)
    }

    /**
     * set tool bar render and behavior with default action
     *
     * @param toolbar target to set up
     */
    protected fun setupToolbar(toolbar: Toolbar) {
        setupToolbar(toolbar, true)
        setupToolbarBackNavigation(toolbar, R.drawable.left_arrow_white)
    }

    /**
     * helper method for render standard tool with status bar
     *
     * @param toolBar       target to be set up
     * @param withStatusBar true if status bar should render with tool bar
     */
    protected fun setupToolbar(toolBar: Toolbar, withStatusBar: Boolean) {
        setSupportActionBar(toolBar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        if (withStatusBar) {
            ImmersionBar.with(this).titleBar(toolBar).init()
        }
    }

    /**
     * helper method for setting up navigation
     */
    protected fun setupToolbarBackNavigation(toolbar: Toolbar, navigationIcon: Int) {
        toolbar.setNavigationIcon(navigationIcon)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    protected fun showProgress(msg: String?) {
        if (loading == null) {
            loading = CommNoneAndroidLoading(this, msg)
        }
        loading?.show()
    }

    protected fun dismissProgress() {
        if (loading != null) {
            loading!!.dismiss()
        }
    }

    /**
     * 在这里可以拦截返回事件并处理它们
     * 例如当你需要点击2次返回按钮退出应用的时候
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                finishAfterTransition()
            } else {
                finish()
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}