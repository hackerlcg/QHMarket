package com.qianhua.market.base

import android.app.Application

/**
 * app全局初始化，以及自定义属性位置
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/11/6
 */
class App : Application(){

    companion object {
        lateinit var instance : App
            private set
            get
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        init()
    }

    private fun init(){

    }
}