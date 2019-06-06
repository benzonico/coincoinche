package org.sonarsource.coincoinche

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_manche.*

class MancheActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manche)
        euxButton.setOnClickListener { view ->
            euxButton.isChecked = true
            nousButton.isChecked = false
        }
        nousButton.setOnClickListener { view ->
            euxButton.isChecked = false
            nousButton.isChecked = true
        }
    }
}
