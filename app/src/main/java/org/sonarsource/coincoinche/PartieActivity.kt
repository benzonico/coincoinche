package org.sonarsource.coincoinche

import android.app.Activity
import android.content.Intent
import android.database.DataSetObserver
import android.graphics.Paint
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class PartieActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var EuxLabel: TextView
    private lateinit var NousLabel: TextView
    private lateinit var EuxScore: TextView
    private lateinit var NousScore: TextView
    private lateinit var game: Game
    private lateinit var adapter: MancheListAdapter

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

        adapter = MancheListAdapter(this, game)
        adapter.registerDataSetObserver(object : DataSetObserver() {
            override fun onChanged() {
                super.onChanged()
                updateGameScore()
            }
        })
        listView.adapter = adapter
        updateGameScore()
    }

    private fun updateGameScore() {
        game.updateScore()
        EuxScore = findViewById(R.id.scoreWholeMancheEux)
        NousScore = findViewById(R.id.scoreWholeMancheNous)
        if (game.eux > game.nous) {
            EuxScore.setTypeface(EuxScore.typeface, Typeface.BOLD)
            NousScore.setTypeface(null, Typeface.NORMAL)

            EuxScore.setTextColor(resources.getColor(R.color.colorPrimary))
            NousScore.setTextColor(resources.getColor(R.color.abc_primary_text_material_dark))
        } else if (game.nous > game.eux) {

            NousScore.setTypeface(EuxScore.typeface, Typeface.BOLD)
            EuxScore.setTypeface(null, Typeface.NORMAL)

            NousScore.setTextColor(resources.getColor(R.color.colorPrimary))
            EuxScore.setTextColor(resources.getColor(R.color.abc_primary_text_material_dark))
        } else {
            EuxScore.setTypeface(null, Typeface.NORMAL)
            NousScore.setTypeface(null, Typeface.NORMAL)

            NousScore.setTextColor(resources.getColor(R.color.abc_primary_text_material_dark))
            EuxScore.setTextColor(resources.getColor(R.color.abc_primary_text_material_dark))
        }
        EuxScore.text = game.eux.toString()
        NousScore.text = game.nous.toString()
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("Game", game)
        setResult(0, intent)
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val manche = data?.getParcelableExtra<Manche>("Manche")
            game.addManche(manche)
            adapter.notifyDataSetChanged()
            updateGameScore()
            updateAGame(game, baseContext)
        }
    }
}
