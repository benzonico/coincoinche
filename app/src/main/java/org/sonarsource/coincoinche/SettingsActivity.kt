package org.sonarsource.coincoinche

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import android.content.ClipData
import android.content.ClipboardManager

class SettingsActivity : AppCompatActivity() {
    fun updateSave(isChecked: Boolean) {
        saveOnline(this, isChecked)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val userId =  id(this)

        userIdText.text = userId
        saveOnlineSwitch.isChecked = saveOnline(this)

        userIdCopy.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(userId, userId)
            clipboard.primaryClip = clip
        }

        saveOnlineSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            updateSave(isChecked)
        }
    }
}
