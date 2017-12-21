package com.qianhua.market.ui.home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qianhua.market.R
import com.qianhua.market.base.glide.GlideA
import com.qianhua.market.entity.HotNews

/**
 * 类说明
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/6
 */
class HotNewsAdapter : BaseQuickAdapter<HotNews, BaseViewHolder>(R.layout.rv_item_hot_news) {
    override fun convert(helper: BaseViewHolder?, item: HotNews?) {
        if(helper != null) {
            if (item?.title != null) {
                helper.setText(R.id.news_title, item.title)
            }

            if (item?.filePath != null) {
                GlideA.with(helper.itemView.context)
                        .load(item.filePath)
                        .centerCrop()
                        .placeholder(R.drawable.image_place_holder)
                        .into(helper.getView(R.id.news_image))
            }else{
                helper.setImageResource(R.id.news_image, R.drawable.image_place_holder)
            }
        }
    }

    fun notifyHotNewsChanged(news: List<HotNews>) {
        setNewData(news)
    }
}