package com.qianhua.market.config


object RequestConstants {
    /**
     * Android平台
     */
    val PLATFORM = 1

    /**
     * 更新密码-重置密码
     */
    val UPDATE_PWD_TYPE_RESET = 2
    /**
     * 更新密码-修改密码
     */
    val UPDATE_PWD_TYPE_CHANGE = 3

    /**
     * 验证码-注册
     */
    val VERIFICATION_TYPE_REGISTER = "0"
    /**
     * 验证码-重置密码
     */
    val VERIFICATION_TYPE_RESET_PWD = "2"

    val SUP_TYPE_AD = 1
    val SUP_TYPE_BANNER = 2
    val SUP_TYPE_DIALOG = 3

    val AVATAR_BYTE_SIZE = 15 * 1024
}
