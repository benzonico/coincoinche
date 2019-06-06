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
    private var game: Game? = null
    private var adapter: MancheListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partie)

        game = intent.getParcelableExtra<Game>("Game")
        fab.setOnClickListener { view ->
            val intent = Intent(this, MancheActivity::class.java).apply {}
            startActivityForResult(intent, 0)
        }

        EuxLabel = findViewById(R.id.labelWholeMancheEux)
        EuxLabel.text = "Eux"
        NousLabel = findViewById(R.id.labelWholeMancheNous)
        NousLabel.text = "Nous"


        listView = findViewById(R.id.manches_list)


        val manches = game?.manches

        adapter = manches?.let { MancheListAdapter(this, it) }
        listView.adapter = adapter



        //A revoir quand le stockage des scores sera fait.
        EuxScore = findViewById(R.id.scoreWholeMancheEux)
        EuxScore.text = Scoreeux.toString()
        NousScore = findViewById(R.id.scoreWholeMancheNous)
        NousScore.text = Scorenous.toString()

    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("Game", game)
        setResult(0, intent)
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val manche = data?.getParcelableExtra<Manche>("Manche")
        game?.addManche(manche)
        adapter?.notifyDataSetChanged()
    }
}
