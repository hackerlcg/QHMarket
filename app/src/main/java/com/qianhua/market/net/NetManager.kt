package com.qianhua.market.net

import com.qianhua.market.config.APIConfig
import com.qianhua.market.net.interceptor.AccessHeadInterceptor
import gear.yc.com.gearlibrary.network.api.GearHttpServiceManager
import gear.yc.com.gearlibrary.network.http.OkHttpManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * 网络访问管理，提供全局的okClient和Retrofit引用
 * @author joker
 * Email:lc@shandaichaoren.com or 812405389@qq.com
 * @version 2017/11/6
 */
class NetManager {
    /**
     * 初始化Ok引用和Retrofit引用
     */
    companion object {
        var mClient : OkHttpClient = OkHttpManager.getInstance()
                .setInterceptorCom(AccessHeadInterceptor())
                .build()
                .client
        var mRetrofit : Retrofit = GearHttpServiceManager.build(APIConfig.BASE_URL,mClient)
    }
}
