package org.sonarsource.coincoinche

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_manche.*

class MancheActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manche)

        var coincheMultiplier = 1
        euxScore.text = "0"
        nousScore.text = "0"
        euxTotal.text = "0"
        nousTotal.text = "0"

        euxButton.setOnClickListener { view ->
            euxButton.isChecked = true
            nousButton.isChecked = false
        }
        nousButton.setOnClickListener { view ->
            euxButton.isChecked = false
            nousButton.isChecked = true
        }
        euxButton2.setOnClickListener { view ->
            nousButton2.isChecked = false
        }
        nousButton2.setOnClickListener { view ->
            euxButton2.isChecked = false
        }

        coincheSans.setOnClickListener {view ->
            coincheAvec.isChecked = false
            coincheSurcoinche.isChecked = false
            coincheMultiplier = 1
        }
        coincheAvec.setOnClickListener { view ->
            coincheSans.isChecked = false
            coincheSurcoinche.isChecked = false
            coincheMultiplier = 2
        }
        coincheSurcoinche.setOnClickListener { view ->
            coincheSans.isChecked = false
            coincheAvec.isChecked = false
            coincheMultiplier = 4
        }

        contractBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress == 0) {
                    contract.text = ""
                } else if (progress == contractBar.max) {
                    contract.text = "Capot"
                } else {
                    contract.text = "" + (70 + progress * 10)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Write code to perform some action when touch is started.
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Write code to perform some action when touch is stopped.
            }
        })

        val scoreListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                refreshScore()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Write code to perform some action when touch is started.
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Write code to perform some action when touch is stopped.
            }
        }
        score_diz.setOnSeekBarChangeListener(scoreListener)

        // TODO à la fin de la partie
        // val myGame = Game()
        // myGame.eux = 42
        // myGame.nous = 2000
        // gamesRef.child(id(this)).child((0..Int.MAX_VALUE).random().toString()).setValue(myGame)
    }

    private fun refreshScore() {
        val nous_score = score_diz.progress// + score_unit.progress
        val eux_score  = 162 - nous_score
        if (nous_score == 0) {
            euxScore.text = "Capot"
            nousScore.text = "0"
        } else if (eux_score == 0) {
            nousScore.text = "Capot"
            euxScore.text = "0"
        } else {
            euxScore.text = eux_score.toString()
            nousScore.text = nous_score.toString()
        }
    }
}
