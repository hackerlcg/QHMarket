package com.qianhua.market.entity


import android.os.Parcel
import android.os.Parcelable

class HotNews() : Parcelable {

    var id: String? = null
    var title: String? = null
    var fileName: String? = null
    var filePath: String? = null
    var explain: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        title = parcel.readString()
        fileName = parcel.readString()
        filePath = parcel.readString()
        explain = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(fileName)
        parcel.writeString(filePath)
        parcel.writeString(explain)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HotNews> {
        override fun createFromParcel(parcel: Parcel): HotNews {
            return HotNews(parcel)
        }

        override fun newArray(size: Int): Array<HotNews?> {
            return arrayOfNulls(size)
        }
    }

}
