package com.qianhua.market.net.api

import com.qianhua.market.config.APIConfig.Companion.BASE_PATH
import com.qianhua.market.config.APIConfig.Companion.PRODUCT_PATH
import com.qianhua.market.entity.*
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * 网络接口
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/12/6
 */
interface MainApi {
    /**
     * 启动页广告，banner，弹窗广告
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/supernatant/querySupernatant")
    fun fetchSupernatant(@Field("port") port: Int, @Field("supernatantType") supernatantType: Int):
            Flowable<ResponseJson<MutableList<AdBanner>>>

    /**
     * 消息中心-公告
     */
    @POST(BASE_PATH + "/notice/home")
    fun noticeHome(): Flowable<ResponseJson<NoticeAbstract>>

    /**
     * 头条滚动信息
     */
    @POST(BASE_PATH + "/supernatant/queryBorrowingScroll")
    fun fetchBorrowingScroll(): Flowable<ResponseJson<MutableList<String>>>


    /**
     * 获取首页热门资讯
     */
    @GET(BASE_PATH + "/information/hotList")
    fun fetchHotNews(): Flowable<ResponseJson<MutableList<HotNews>>>


    /**
     * 获取首页热门贷款产品
     */
    @GET(PRODUCT_PATH + "/product/hotList")
    fun fetchHotLoanProducts(): Flowable<ResponseJson<MutableList<LoanProduct.Row>>>

    /**
     * 查询贷款产品列表
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/list")
    fun fetchLoanProduct(@Field("amount") amount: Double, @Field("dueTime") dueTime: String,
                                  @Field("orientCareer") pro: String, @Field("pageNo") pageNum: Int,
                                  @Field("pageSize") pageSize: Int): Flowable<ResponseJson<LoanProduct>>

    /**
     * 资讯列表
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/information/list")
    fun fetchNews(@Field("pageNo") pageNum: Int, @Field("pageSize") pageSize: Int): Flowable<ResponseJson<News>>

}