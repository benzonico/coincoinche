package org.sonarsource.coincoinche

import android.os.Parcel
import android.os.Parcelable
import java.util.*
import kotlin.collections.ArrayList

class Game() : Parcelable {
    var eux = 0
    var nous = 0
    var date = Calendar.getInstance().timeInMillis
    var manches = ArrayList<Manche>();

    constructor(parcel: Parcel) : this() {
        eux = parcel.readInt()
        nous = parcel.readInt()
        date = parcel.readLong()
        manches = parcel.readArrayList(Manche.javaClass.classLoader) as ArrayList<Manche>
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(eux)
        parcel.writeInt(nous)
        parcel.writeLong(date)
        parcel.writeList(manches)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun addManche(manche: Manche?) {
        if(manche == null) {
            return
        }
        manches.add(manche)
    }

    fun updateScore() {
        eux = manches.map { m -> m.eux }.sum();
        nous = manches.map { m -> m.nous }.sum();
    }

    companion object CREATOR : Parcelable.Creator<Game> {
        override fun createFromParcel(parcel: Parcel): Game {
            return Game(parcel)
        }

        override fun newArray(size: Int): Array<Game?> {
            return arrayOfNulls(size)
        }
    }
}
