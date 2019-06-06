package org.sonarsource.coincoinche

import android.graphics.Color
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

    var coincheMultiplier = 1

    private fun computeScores() {
        if (!euxButton.isChecked && !nousButton.isChecked) return
        if (contractBar.progress == 0) return
        if (euxScore.text.toString() == "0" && nousScore.text.toString() == "0") return

        val nousContract = nousButton.isChecked
        var contract = (70 + contractBar.progress * 10)
        val isCapot = contract == 190
        if(isCapot) {
           contract = 250
        }
        contract *= coincheMultiplier

        var nousScoreValue = 0
        try {
            nousScoreValue = nousScore.text.toString().toInt()
        } catch (e: NumberFormatException) {
            return
        }

        val euxScoreValue = 162 - nousScoreValue

        var nousTotalValue: Int
        var euxTotalValue: Int

        if (nousContract) {
            if (nousScoreValue >= contract) {
                // Partie faite => à arrondir
                nousTotalValue = contract + round(nousScoreValue)
                euxTotalValue = round(euxScoreValue)
            } else {
                // nous sommes dedans
                nousTotalValue = 0
                euxTotalValue = round(162) + contract
            }
        } else {
            if (euxScoreValue >= contract) {
                // Partie faite => à arrondir
                euxTotalValue = contract + round(euxScoreValue)
                nousTotalValue = round(nousScoreValue)
            } else {
                // eux sont dedans
                euxTotalValue = 0
                nousTotalValue = round(162) + contract
            }
        }

        // Belote

        // 0 sans être capot ?
        // Partir en étant capot ? (capot inversé)

    }

    private fun round(scoreValue: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manche)

        euxScore.text = "0".toEditable()
        nousScore.text = "0".toEditable()
        euxTotal.text = "0"
        nousTotal.text = "0"

        euxButton.setOnClickListener { view ->
            euxButton.isChecked = true
            nousButton.isChecked = false
            computeScores()
        }
        nousButton.setOnClickListener { view ->
            euxButton.isChecked = false
            nousButton.isChecked = true
            computeScores()
        }
        euxButton2.setOnClickListener { view ->
            nousButton2.isChecked = false
            computeScores()
        }
        nousButton2.setOnClickListener { view ->
            euxButton2.isChecked = false
            computeScores()
        }

        coincheSans.setOnClickListener {view ->
            coincheAvec.isChecked = false
            coincheSurcoinche.isChecked = false
            coincheMultiplier = 1
            computeScores()
        }
        coincheAvec.setOnClickListener { view ->
            coincheSans.isChecked = false
            coincheSurcoinche.isChecked = false
            coincheMultiplier = 2
            computeScores()
        }
        coincheSurcoinche.setOnClickListener { view ->
            coincheSans.isChecked = false
            coincheAvec.isChecked = false
            coincheMultiplier = 4
            computeScores()
        }

        fab.setOnClickListener { view ->
            println("scores !")
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
