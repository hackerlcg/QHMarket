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
import kotlinx.android.synthetic.main.dialog_brzhiye.view.*


class ProFilterPopup(context: Activity, private val shadowView: View, private val tv: TextView, private val iv: ImageView) : PopupWindow(), View.OnClickListener {

    private var listener: onBrZhiyeListener? = null

    init {


        shadowView.visibility = View.VISIBLE
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        contentView = inflater.inflate(R.layout.dialog_brzhiye, null)

        this.width = LayoutParams.MATCH_PARENT
        this.height = LayoutParams.WRAP_CONTENT
        this.isFocusable = true
        this.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        tv.setTextColor(Color.parseColor("#5591FF"))
        iv.setImageResource(R.drawable.daosanjiao_blue)
        rotateArrow(0f, 180f, iv)

        contentView.ly_1.setOnClickListener(this)
        contentView.ly_2.setOnClickListener(this)
        contentView.ly_3.setOnClickListener(this)
        contentView.ly_4.setOnClickListener(this)
    }


    override fun dismiss() {
        super.dismiss()
        shadowView.visibility = View.GONE
        tv.setTextColor(Color.parseColor("#424251"))
        iv.setImageResource(R.drawable.daosanjiao)
        rotateArrow(180f, 0f, iv)
    }


    override fun onClick(view: View) {
        val selectedIndex: Int = when (view.id) {
            R.id.ly_1 -> 0
            R.id.ly_2 -> 1
            R.id.ly_3 -> 2
            R.id.ly_4 -> 3
            else -> -1
        }

        if (listener != null) {
            listener!!.onZhiyeItemClick(selectedIndex)
        }
        dismiss()
    }

    fun setShareItemListener(listener: onBrZhiyeListener) {
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


    interface onBrZhiyeListener {
        fun onZhiyeItemClick(selectIndex: Int)
    }
}
