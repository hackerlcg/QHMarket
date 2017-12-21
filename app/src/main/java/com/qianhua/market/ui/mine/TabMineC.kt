package com.qianhua.market.ui.mine

import com.qianhua.market.base.contract.BaseContract
import com.qianhua.market.helper.UserHelper

/**
 * 我的页面接口
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/8
 */
interface TabMineC {
    interface Presenter<T>: BaseContract.BasePresenter<T> {
        fun checkMessage()

        fun checkUserProfile()

        fun checkInvitation()

        fun checkHelpAndFeedback()

        fun checkSetting()
    }

    interface View: BaseContract.BaseView {

        fun showProfile(profile: UserHelper.Profile)

        fun navigateLogin()

        fun navigateUserProfile(userId: String)

        fun navigateMessage(userId: String)

        fun navigateInvitation(userId: String)

        fun navigateHelpAndFeedback(userId: String)

        fun navigateSetting(userId: String?)
    }
}