package org.sonarsource.coincoinche

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Manche() : Parcelable {

    var eux = 0
    var nous = 0
    val date = Calendar.getInstance().timeInMillis

    override fun describeContents(): Int {
       return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(eux)
        parcel.writeInt(nous)
    }

    constructor(parcel: Parcel) : this() {
        eux = parcel.readInt()
        nous = parcel.readInt()
    }

    companion object CREATOR : Parcelable.Creator<Manche> {
        override fun createFromParcel(parcel: Parcel): Manche {
            return Manche(parcel)
        }

        override fun newArray(size: Int): Array<Manche?> {
            return arrayOfNulls(size)
        }
    }
}
