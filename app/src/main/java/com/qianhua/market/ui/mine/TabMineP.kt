package com.qianhua.market.ui.mine

import android.content.Context
import com.qianhua.market.base.contract.presenter.PresenterDataWrapper
import com.qianhua.market.helper.UserHelper

/**
 * 我的页面presenter
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/8
 */
class TabMineP(v: TabMineC.View,c :Context): PresenterDataWrapper<UserHelper,TabMineC.View>(v),
        TabMineC.Presenter<UserHelper>{
    init {
        mData = UserHelper.getInstance(c)
    }
    override fun checkMessage() {
        if (checkValidUser()) mView.navigateMessage(mData.profile.id)
    }

    override fun checkUserProfile() {
        if (checkValidUser()) mView.navigateUserProfile(mData.profile.id)
    }

    override fun checkInvitation() {
        if (checkValidUser()) mView.navigateInvitation(mData.profile.id)
    }

    override fun checkHelpAndFeedback() {
        if (checkValidUser()) mView.navigateHelpAndFeedback(mData.profile.id)
    }

    override fun checkSetting() {
        if (checkValidUser()) mView.navigateSetting(null)
    }

    override fun fetch() {
        var profile = mData.profile
        if (profile != null){
            mView.showProfile(profile)
        }
    }

    override fun refreshData() {
    }

    override fun getData(): UserHelper {
        return mData
    }

    override fun close() {
    }

    private fun checkValidUser(): Boolean {
        if (mData.profile == null) {
            mView.navigateLogin()
            return false
        }
        return true
    }

}