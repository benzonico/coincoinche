package org.sonarsource.coincoinche

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject
import java.util.*

class Manche(var nousPreneur:Boolean = true, var eux: Int = 0, var nous: Int = 0, var couleur:Int = 0, private val date: Long = Calendar.getInstance().timeInMillis) : Parcelable {

    override fun describeContents(): Int {
       return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(if (nousPreneur) 1 else 0)
        parcel.writeInt(eux)
        parcel.writeInt(nous)
        parcel.writeInt(couleur)
    }

    fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("nousPreneur", nousPreneur)
        jsonObject.put("eux", eux)
        jsonObject.put("nous", nous)
        jsonObject.put("couleur", couleur)
        jsonObject.put("date", date)
        return jsonObject
    }

    fun euxScore(): String {
        var res = ""
        if(!nousPreneur) {
            res += color() + " "
        }
        return res + eux
    }

    fun nousScore(): String {
        var res = ""
        if(nousPreneur) {
            res += color() + " "
        }
        return res + nous
    }

    fun color():String {
        return when (couleur) {
            0 -> "♥"
            1 -> "♠"
            2 -> "♦"
            3 -> "♣"
            else -> ""
        }
    }

    constructor(parcel: Parcel) : this() {
        nousPreneur = parcel.readInt() != 0
        eux = parcel.readInt()
        nous = parcel.readInt()
        couleur = parcel.readInt()
    }

    companion object CREATOR : Parcelable.Creator<Manche> {
        override fun createFromParcel(parcel: Parcel): Manche {
            return Manche(parcel)
        }

        override fun newArray(size: Int): Array<Manche?> {
            return arrayOfNulls(size)
        }

        fun fromJson(jsonManche: JSONObject): Manche {
            return Manche(
                jsonManche.getBoolean("nousPreneur"),
                jsonManche.getInt("eux"),
                jsonManche.getInt("nous"),
                jsonManche.getInt("couleur"),
                jsonManche.getLong("date"))
        }
    }
}
