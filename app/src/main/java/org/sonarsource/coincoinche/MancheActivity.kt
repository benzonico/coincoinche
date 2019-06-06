package org.sonarsource.coincoinche

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_manche.*
import kotlinx.android.synthetic.main.game_row_layout.*
import java.lang.NumberFormatException

class MancheActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manche)

        var coincheMultiplier = 1
        euxScore.text = "0".toEditable()
        nousScore.text = "0".toEditable()
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
        score_slider.setOnSeekBarChangeListener(scoreListener)
        euxScore.addTextChangedListener(object :TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                try {
                    val intScore = s.toString().toInt()
                    euxScore.placeCursorToEnd()
                    if (intScore != 162 - score_slider.progress) {
                        score_slider.progress = 162 - intScore
                    }
                } catch (e: NumberFormatException) {
                    // do nothing
                }
            }

        })
        nousScore.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                try {
                    val intScore = s.toString().toInt()
                    if (intScore != score_slider.progress) {
                        score_slider.progress = intScore
                    }
                    nousScore.placeCursorToEnd()
                } catch (e: NumberFormatException) {
                    // do nothing
                }
            }

        })


        // TODO à la fin de la partie
        // val myGame = Game()
        // myGame.eux = 42
        // myGame.nous = 2000
        // gamesRef.child(id(this)).child((0..Int.MAX_VALUE).random().toString()).setValue(myGame)
    }

    private fun refreshScore() {
        val nous_score = score_slider.progress// + score_unit.progress
        val eux_score  = 162 - nous_score
        if (nous_score == 0) {
            euxScore.text = "Capot".toEditable()
            nousScore.text = "0".toEditable()
        } else if (eux_score == 0) {
            nousScore.text = "Capot".toEditable()
            euxScore.text = "0".toEditable()
        } else {
            euxScore.text = eux_score.toString().toEditable()
            nousScore.text = nous_score.toString().toEditable()
        }
    }

    // handy extension methods
    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
    fun EditText.placeCursorToEnd() {
        this.setSelection(this.text.length)
    }

}
