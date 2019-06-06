package org.sonarsource.coincoinche

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class PartieActivity : AppCompatActivity() {

    private lateinit var listView: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partie)

        listView = findViewById(R.id.manches_list)
        val listItems = arrayOfNulls<String>(24)
        for (i in 0 until 24) {
            listItems[i] = "PartieActivity"+i
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter
    }
}
