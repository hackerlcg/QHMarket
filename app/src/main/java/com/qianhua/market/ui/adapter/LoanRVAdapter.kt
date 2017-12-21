package com.qianhua.market.ui.adapter

import android.graphics.Color
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qianhua.market.R
import com.qianhua.market.base.glide.GlideA
import com.qianhua.market.entity.LoanProduct

/**
 * 类说明
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/6
 */
class LoanRVAdapter(data: MutableList<LoanProduct.Row>): BaseQuickAdapter<LoanProduct.Row, BaseViewHolder>(R.layout.rv_item_loan) {

    private val tagIds = intArrayOf(R.id.tag_1, R.id.tag_2)
    init {
        setNewData(data)
    }
    override fun convert(helper: BaseViewHolder, item: LoanProduct.Row) {
        //参考日月息
        if (item.interestTimeText != null) {
            helper.setText(R.id.interest_text, item.interestTimeText)
        }
        //logo
        if (!TextUtils.isEmpty(item.logoUrl)) {
            GlideA.with(helper.itemView.context)
                    .load(item.logoUrl)
                    .centerCrop()
                    .placeholder(R.drawable.image_place_holder)
                    .into(helper.getView(R.id.loan_image))
        } else {
            helper.setImageResource(R.id.loan_image, R.drawable.image_place_holder)
        }
        //name
        if (item.productName != null) {
            helper.setText(R.id.loan_name, item.productName)
        }
        //tags
        val feature = if (item.feature != null) item.feature?.split(",") else null
        var visibleCount = 0
        if (feature != null && feature.isNotEmpty() && "" != feature[0]) {
            var i = 0
            while (i < feature.size && i < tagIds.size) {
                helper.setVisible(tagIds[i], true)
                helper.setText(tagIds[i], feature[i])
                visibleCount++
                ++i
            }
        }
        for (i in tagIds.size - 1 downTo visibleCount){
            helper.setVisible(tagIds[i], false);
        }
        //loaned number
        var ss = SpannableString("成功借款${item.successCount}人")
        ss.setSpan(ForegroundColorSpan(Color.parseColor("#ff395e")), 4, ss.length - 1,
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE)
        helper.setText(R.id.loaned_number, ss)
        //amount
        if (item.borrowingHighText != null) {
            helper.setText(R.id.loan_max_amount, item.borrowingHighText)
        }
        //interest
        if (item.interestLowText != null) {
            helper.setText(R.id.loan_interests, item.interestLowText)
        }
        //time
        if (item.dueTimeText != null) {
            helper.setText(R.id.loan_time_range, item.dueTimeText)
        }
        //badge
        var sign = getLabelIcon(item.productSign)
        if (sign != -1) {
            helper.setVisible(R.id.loan_badge, true)
            helper.setImageResource(R.id.loan_badge, sign)
        } else {
            helper.setVisible(R.id.loan_badge, false)
        }
    }

    private fun getLabelIcon(type: Int): Int{
        return when (type){
            1 -> R.drawable.label_new
            2 -> R.drawable.label_hot
            3 -> R.drawable.label_jian
            4 -> R.drawable.label_featured
            else -> -1
        }
    }
}