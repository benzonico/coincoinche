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

        EuxLabel = findViewById(R.id.labelWholeMancheEux)
        EuxLabel.text = "Eux"
        NousLabel = findViewById(R.id.labelWholeMancheNous)
        NousLabel.text = "Nous"


        listView = findViewById(R.id.manches_list)


        val  game = intent.getParcelableExtra<Game>("Game")
        val manches = game.manches

        val adapter = MancheListAdapter(this, manches)
        listView.adapter = adapter



        //A revoir quand le stockage des scores sera fait.
        EuxScore = findViewById(R.id.scoreWholeMancheEux)
        EuxScore.text = Scoreeux.toString()
        NousScore = findViewById(R.id.scoreWholeMancheNous)
        NousScore.text = Scorenous.toString()

    }
}
