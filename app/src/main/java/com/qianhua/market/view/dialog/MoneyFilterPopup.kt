package com.qianhua.market.view.dialog

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputFilter
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.qianhua.market.R
import com.qianhua.market.config.Constant
import com.qianhua.market.utils.InputMethodUtil
import com.qianhua.market.utils.ToastUtils
import kotlinx.android.synthetic.main.dialog_brmoney.view.*


class MoneyFilterPopup(private val context: Activity, money: String, private val shadowView: View, private val tv: TextView, private val iv: ImageView) : PopupWindow(context) {

    private var listener: onBrMoneyListener? = null

    init {

        shadowView.visibility = View.VISIBLE
        shadowView.invalidate()
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        contentView = inflater.inflate(R.layout.dialog_brmoney, null)

        width = LayoutParams.MATCH_PARENT
        height = LayoutParams.WRAP_CONTENT
        isFocusable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        tv.setTextColor(Color.parseColor("#5591FF"))
        iv.setImageResource(R.drawable.daosanjiao_blue)
        rotateArrow(0f, 180f, iv)

        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            if (dstart == 0 && !TextUtils.isEmpty(source)) {
                if (source == "0") {
                    return@InputFilter ""
                }
            }
            null
        }
        var filters: Array<InputFilter?>? = contentView.money.filters
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
        contentView.money.filters = filters
        contentView.money.setText(money)
        contentView.money.setSelection(contentView.money.text.length)
        contentView.money.postDelayed({ InputMethodUtil.openSoftKeyboard(context, contentView.money) }, 200)

        contentView.cancel.setOnClickListener{
            dismiss()
        }
        contentView.confirm.setOnClickListener {
            val money = contentView.money.text.toString()
            if (TextUtils.isEmpty(money)) {
                ToastUtils.showShort(context, "请输入金额", null)
                return@setOnClickListener
            }
            try {
                val amount = Integer.parseInt(money)
                if (amount < Constant.MIN_FILTER_MONEY) {
                    ToastUtils.showShort(context, "最低借款" + Constant.MIN_FILTER_MONEY + "元", null)
                    return@setOnClickListener
                }
                dismiss()
                if (listener != null) {
                    listener!!.onMoneyItemClick(amount)
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                ToastUtils.showShort(context, "请输入正确金额", null)
            }
        }
    }


    override fun dismiss() {
        InputMethodUtil.closeSoftKeyboard(context, contentView.money)
        super.dismiss()
        shadowView.visibility = View.GONE
        tv.setTextColor(Color.parseColor("#424251"))
        iv.setImageResource(R.drawable.daosanjiao)
        rotateArrow(180f, 0f, iv)
    }

    fun setShareItemListener(listener: onBrMoneyListener) {
        this.listener = listener
    }

    private fun rotateArrow(fromDegrees: Float, toDegrees: Float, imageView: ImageView) {
        val mRotateAnimation = RotateAnimation(fromDegrees, toDegrees,
                (imageView.measuredWidth / 2.0).toInt().toFloat(),
                (imageView.measuredHeight / 2.0).toInt().toFloat())
        mRotateAnimation.duration = 150
        mRotateAnimation.fillAfter = true
        imageView.startAnimation(mRotateAnimation)
    }

    interface onBrMoneyListener {
        fun onMoneyItemClick(money: Int)
    }

}
