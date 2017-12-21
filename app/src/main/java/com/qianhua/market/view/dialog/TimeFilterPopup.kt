package com.qianhua.market.view.dialog

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.qianhua.market.R
import kotlinx.android.synthetic.main.dialog_brtime.view.*

class TimeFilterPopup(context: Activity, selectTimeIndex: Int, private val shadowView: View, private val tv: TextView, private val iv: ImageView,
                      tags: Array<String>) : PopupWindow(context) {

    private var listener: onBrTimeListener? = null

    init {

        shadowView.visibility = View.VISIBLE
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        contentView = inflater.inflate(R.layout.dialog_brtime, null)

        this.width = LayoutParams.MATCH_PARENT
        this.height = LayoutParams.WRAP_CONTENT
        this.isFocusable = true
        this.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        tv.setTextColor(Color.parseColor("#5591FF"))
        iv.setImageResource(R.drawable.daosanjiao_blue)
        rotateArrow(0f, 180f, iv)

        contentView.flow_layout.setTags(tags, selectTimeIndex)
        contentView.flow_layout.setTagSelectedListener({ selected ->
            if (listener != null) {
                listener!!.onTimeItemClick(selected)
            }
            dismiss()
        })

    }


    override fun dismiss() {
        super.dismiss()
        shadowView.visibility = View.GONE
        tv.setTextColor(Color.parseColor("#424251"))
        iv.setImageResource(R.drawable.daosanjiao)
        rotateArrow(180f, 0f, iv)
    }

    fun setShareItemListener(listener: onBrTimeListener) {
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


    interface onBrTimeListener {
        fun onTimeItemClick(selectTimeIndex: Int)
    }
}
