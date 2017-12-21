package com.qianhua.market.entity


import android.os.Parcel
import android.os.Parcelable

class News {

    var total: Int = 0
    var rows: MutableList<Row>? = null

    class Row : Parcelable {
        var explain: String? = null
        var image: String? = null
        var pv: Int = 0
        var source: String? = null
        var gmtCreate: Long = 0
        var title: String? = null
        var id: String? = null

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(explain)
            dest.writeString(image)
            dest.writeInt(pv)
            dest.writeString(source)
            dest.writeLong(gmtCreate)
            dest.writeString(title)
            dest.writeString(id)
        }

        constructor(parcel: Parcel) {
            explain = parcel.readString()
            image = parcel.readString()
            pv = parcel.readInt()
            source = parcel.readString()
            gmtCreate = parcel.readLong()
            title = parcel.readString()
            id = parcel.readString()
        }

        companion object CREATOR: Parcelable.Creator<Row>{
                override fun createFromParcel(source: Parcel): Row {
                    return Row(source)
                }

                override fun newArray(size: Int): Array<Row?> {
                    return arrayOfNulls(size)
                }
        }
    }
}
