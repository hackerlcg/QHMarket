package com.qianhua.market.entity


import android.os.Parcel
import android.os.Parcelable

class LoanProduct {

    var total: Int = 0
    var rows: MutableList<Row>? = null

    class Row : Parcelable {
        /**
         * 是否是快捷推荐 0不是，1是
         */
        var quickCommend: Int = 0
        /**
         * 最低利率
         */
        var interestLowText: String? = null
        /**
         * 期限范围
         */
        var dueTimeText: String? = null
        var logoUrl: String? = null
        /**
         * 成功借款人数
         */
        var successCount: Int = 0
        var id: String? = null
        /**
         * 产品标签， 0--无 1--最新 2--热门 3--强力推荐 4--精选
         */
        var productSign: Int = 0
        var productName: String? = null
        /**
         * tags,spilt by ,
         */
        var feature: String? = null
        /**
         * 借款额度
         */
        var borrowingHighText: String? = null
        /**
         * 借款利息类型，日，月
         */
        var interestTimeTypeText: String? = null

        var interestTimeText: String? = null

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(quickCommend)
            dest.writeString(interestLowText)
            dest.writeString(dueTimeText)
            dest.writeString(logoUrl)
            dest.writeInt(successCount)
            dest.writeString(id)
            dest.writeInt(productSign)
            dest.writeString(productName)
            dest.writeString(feature)
            dest.writeString(borrowingHighText)
            dest.writeString(interestTimeTypeText)
            dest.writeString(interestTimeText)
        }

        constructor(parcel: Parcel) {
            quickCommend = parcel.readInt()
            interestLowText = parcel.readString()
            dueTimeText = parcel.readString()
            logoUrl = parcel.readString()
            successCount = parcel.readInt()
            id = parcel.readString()
            productSign = parcel.readInt()
            productName = parcel.readString()
            feature = parcel.readString()
            borrowingHighText = parcel.readString()
            interestTimeTypeText = parcel.readString()
            interestTimeText = parcel.readString()
        }
        companion object CREATOR : Parcelable.Creator<Row> {
            override fun createFromParcel(parcel: Parcel): Row {
                return Row(parcel)
            }

            override fun newArray(size: Int): Array<Row?> {
                return arrayOfNulls(size)
            }
        }
    }
}
