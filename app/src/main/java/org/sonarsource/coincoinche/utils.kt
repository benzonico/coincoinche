package org.sonarsource.coincoinche

import android.content.Context
import java.util.*

private var uniqueID: String? = null
private val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"
@Synchronized
fun id(context: Context): String {
    if (uniqueID == null) {
        val sharedPrefs = context.getSharedPreferences(
            PREF_UNIQUE_ID, Context.MODE_PRIVATE
        )
        uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null)
        if (uniqueID == null) {
            uniqueID = UUID.randomUUID().toString()
            val editor = sharedPrefs.edit()
            editor.putString(PREF_UNIQUE_ID, uniqueID)
            editor.commit()
        }
    }
    return uniqueID as String
}