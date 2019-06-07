package org.sonarsource.coincoinche

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject
import java.util.*

class Manche(var eux: Int = 0, var nous: Int = 0, private val date: Long = Calendar.getInstance().timeInMillis) : Parcelable {

    override fun describeContents(): Int {
       return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(eux)
        parcel.writeInt(nous)
    }

    fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("eux", eux)
        jsonObject.put("nous", nous)
        jsonObject.put("date", date)
        return jsonObject
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

        fun fromJson(jsonManche: JSONObject): Manche {
            return Manche(jsonManche.getInt("eux"), jsonManche.getInt("nous"), jsonManche.getLong("date"))
        }
    }
}
