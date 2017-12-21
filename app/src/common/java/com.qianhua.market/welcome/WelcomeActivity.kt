package com.qianhua.market.ui.welcome

import android.content.Intent
import android.os.Bundle
import com.qianhua.market.R
import com.qianhua.market.base.activity.BaseActivity
import com.qianhua.market.ui.main.MainActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * 欢迎页面
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/5
 */
class WelcomeActivity : BaseActivity(R.layout.activity_welcome) {

    private val TIME = 3000L
    override fun initUI() {
    }

    override fun attachPresenter() {
    }

    override fun initData() {
        Observable.timer(TIME, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycleDestroy())
                .subscribe({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                })
    }
}
