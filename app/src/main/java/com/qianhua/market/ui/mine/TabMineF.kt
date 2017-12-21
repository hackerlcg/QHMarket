package com.qianhua.market.ui.mine

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.qianhua.market.R
import com.qianhua.market.base.fragment.BasePTabFragment
import com.qianhua.market.base.glide.GlideA
import com.qianhua.market.entity.busevents.UserLogoutEvent
import com.qianhua.market.helper.UserHelper
import com.qianhua.market.utils.CommonUtils
import com.qianhua.market.utils.FastClickUtils
import com.qianhua.market.utils.LegalInputUtils
import com.qianhua.market.utils.ToastUtils
import com.trello.rxlifecycle2.LifecycleTransformer
import kotlinx.android.synthetic.main.fragment_tab_home.*
import kotlinx.android.synthetic.main.fragment_tab_mine.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * 类说明
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/8
 */
class TabMineF: BasePTabFragment<TabMineC.Presenter<UserHelper>>(R.layout.fragment_tab_mine),
        TabMineC.View {
    private var pendingPhone: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //        //umeng统计
//        Statistic.onEvent(Events.ENTER_MINE_PAGE)
        EventBus.getDefault().register(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.fetch()
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.avatar -> if (!FastClickUtils.isFastClick()) {
                mPresenter.checkUserProfile()
            }
            R.id.mine_msg -> {
                //umeng统计
//                Statistic.onEvent(Events.MINE_CLICK_MESSAGE)

                if (!FastClickUtils.isFastClick()) {
                    mPresenter.checkMessage()
                }
            }
            R.id.invite_friend -> {
                //umeng统计
//                Statistic.onEvent(Events.MINE_CLICK_INVITATION)

                if (!FastClickUtils.isFastClick()) {
                    mPresenter.checkInvitation()
                }
            }
            R.id.helper_feedback -> {
                //umeng统计
//                Statistic.onEvent(Events.MINE_CLICK_HELP_FEEDBACK)

                if (!FastClickUtils.isFastClick()) {
                    mPresenter.checkHelpAndFeedback()
                }
            }
            R.id.settings -> {
                //umeng统计
//                Statistic.onEvent(Events.MINE_CLICK_SETTING)

                if (!FastClickUtils.isFastClick()) {
                    mPresenter.checkSetting()
                }
            }
            R.id.login -> if (!FastClickUtils.isFastClick()) {
                navigateLogin()
            }
            R.id.user_name -> if (!FastClickUtils.isFastClick()) {
                mPresenter.checkUserProfile()
            }
            else -> {
            }
        }
    }

    override fun attachPresenter() {
        mPresenter = TabMineP(this, context)
    }

    override fun initUI() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        avatar.setOnClickListener(this)
        mine_msg.setOnClickListener(this)
        invite_friend.setOnClickListener(this)
        helper_feedback.setOnClickListener(this)
        settings.setOnClickListener(this)
        login.setOnClickListener(this)
        user_name.setOnClickListener(this)
    }

    override fun initData() {
    }

    override fun updateUI() {
    }

    override fun showToast(str: String) {
        ToastUtils.showShort(context, str, null)
    }

    override fun showProfile(profile: UserHelper.Profile) {
        login.visibility = View.GONE
        user_name.visibility = View.VISIBLE
        if (profile.headPortrait != null) {
            GlideA.with(context)
                    .load(profile.headPortrait)
                    .into(avatar)
        }
        var userName = profile.userName
        if (userName != null) {
            if (LegalInputUtils.validatePhone(userName)){
                user_name.text = CommonUtils.phone2Username(userName)
            } else {
                user_name.text = userName
            }
        }
    }

    override fun onDialog(show: Boolean) {
    }

    override fun navigateLogin() {
        if (pendingPhone != null) {
//            UserAuthorizationActivity.launch(activity, pendingPhone)
            pendingPhone = null
        } else {
//            UserAuthorizationActivity.launch(activity, null)
        }
    }

    override fun navigateUserProfile(userId: String) {
//        val toUserProfile = Intent(activity, UserProfileActivity::class.java)
//        startActivity(toUserProfile)
    }

    override fun navigateMessage(userId: String) {
//        val toMsg = Intent(activity, MessageCenterActivity::class.java)
//        startActivity(toMsg)
    }

    override fun navigateInvitation(userId: String) {
//        val toInviteFriend = Intent(activity, InvitationActivity::class.java)
//        startActivity(toInviteFriend)
    }

    override fun <R> getLifecycle2(): LifecycleTransformer<R> {
        return bindToLifecycle()
    }

    override fun navigateHelpAndFeedback(userId: String) {
//        val toHelp = Intent(activity, HelperAndFeedbackActivity::class.java)
//        startActivity(toHelp)
    }

    override fun <R> getLifecycleDestroy(): LifecycleTransformer<R> {
        return bindToLifecycleDestroy()
    }

    override fun navigateSetting(userId: String?) {
//        val toSettings = Intent(activity, SettingsActivity::class.java)
//        startActivity(toSettings)
    }

    @Subscribe
    fun onLogout(event: UserLogoutEvent) {
        login.visibility = View.VISIBLE
        user_name.visibility = View.GONE

        GlideA.with(context)
                .load(R.drawable.mine_head_icon)
                .into(avatar)

        pendingPhone = event.pendingPhone
        if (event.pendingAction != null && event.pendingAction.equals(UserLogoutEvent.ACTION_START_LOGIN)
                && view != null) {
            view!!.postDelayed({ navigateLogin() }, 100)
        }
    }
}