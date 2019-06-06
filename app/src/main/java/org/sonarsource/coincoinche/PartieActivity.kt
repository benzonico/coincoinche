package org.sonarsource.coincoinche

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class PartieActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var EuxLabel: TextView
    private lateinit var NousLabel: TextView
    private lateinit var EuxScore: TextView
    private lateinit var NousScore: TextView
    private var Scoreeux: Int = 0
    private var Scorenous: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partie)


        fab.setOnClickListener { view ->
            val intent = Intent(this, MancheActivity::class.java).apply {}
            startActivity(intent)
        }

        /*listView = findViewById(R.id.parties_list)
        val listItems = arrayOfNulls<String>(24)
        for (i in 0 until 24) {
            listItems[i] = "PartieActivity"+i
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter*/


        EuxLabel = findViewById(R.id.labelWholeMancheEux)
        EuxLabel.text = "Eux"
        NousLabel = findViewById(R.id.labelWholeMancheNous)
        NousLabel.text = "Nous"



        listView = findViewById(R.id.manches_list)
        val manches = ArrayList<Manche>()
        for (i in 0 until 24) {
            val myManche = Manche()
            myManche.eux = i * 1
            myManche.nous = i * 10
            manches.add(myManche)
            Scoreeux += myManche.eux
            Scorenous += myManche.nous

        }

        val adapter = MancheListAdapter(this, manches)
        listView.adapter = adapter

        EuxScore = findViewById(R.id.scoreWholeMancheEux)
        EuxScore.text = Scoreeux.toString()
        NousScore = findViewById(R.id.scoreWholeMancheNous)
        NousScore.text = Scorenous.toString()


    }
}
