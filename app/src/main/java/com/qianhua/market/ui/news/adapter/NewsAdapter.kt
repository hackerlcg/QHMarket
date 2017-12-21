package com.qianhua.market.ui.news.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qianhua.market.R
import com.qianhua.market.base.glide.GlideA
import com.qianhua.market.entity.News
import com.qianhua.market.utils.DateFormatUtils

/**
 * 新闻列表adapter
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/8
 */
class NewsAdapter(d: MutableList<News.Row>, c: Context): BaseQuickAdapter<News.Row, BaseViewHolder>(R.layout.rv_item_news) {
    init {
        mContext = c
        setNewData(d)
    }
    override fun convert(helper: BaseViewHolder, item: News.Row) {
        if (item.image != null){
            GlideA.with(mContext)
                    .load(item.image)
                    .centerCrop()
                    .placeholder(R.drawable.image_place_holder)
                    .into(helper.getView(R.id.news_image))
        } else {
            helper.setImageResource(R.id.news_image, R.drawable.image_place_holder)
        }
        if (item.title != null) {
            helper.setText(R.id.news_title, item.title)
        }
        if (item.source != null) {
            helper.setText(R.id.news_source, item.source)
        }
        helper.setText(R.id.news_publish_time, DateFormatUtils.generateNewsDate(item.gmtCreate))
        helper.setText(R.id.news_read_times, "${item.pv}阅读")
    }
}