package org.sonarsource.coincoinche

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_manche.*
import java.lang.NumberFormatException
import kotlin.math.max
import android.R.id.message
import android.content.Intent



class MancheActivity : AppCompatActivity() {

    var coincheMultiplier = 1
    var couleur = ""

    private fun computeScores() {
        if (!euxButton.isChecked && !nousButton.isChecked) return
        if (contractBar.progress == 0) return
        if (euxScore.text.toString() == "0" && nousScore.text.toString() == "0") return

        val nousContract = nousButton.isChecked
        var contract = (70 + contractBar.progress * 10)
        val isCapot = contract == 190
        var contractValue = contract
        if (isCapot) {
            contractValue = 250
        }
        contractValue *= coincheMultiplier

        var nousScoreValue = 0
        var euxScoreValue = 0
        try {
            nousScoreValue = nousScore.text.toString().toInt()
        } catch (e: NumberFormatException) {
            // Capot!
            nousScoreValue = 250
            euxScoreValue = 0
        }
        try {
            euxScoreValue = euxScore.text.toString().toInt()
        } catch (e: NumberFormatException) {
            // Capot!
            nousScoreValue = 0
            euxScoreValue = 250
        }


        var nousTotalValue: Int
        var euxTotalValue: Int

        if (nousContract) {
            // si score 80 la partie est perdu (l'adversaire à 82)
            if (nousScoreValue > 80 && nousScoreValue >= contract) {
                // Partie faite => à arrondir
                nousTotalValue = contractValue + round(nousScoreValue)
                euxTotalValue = if (coincheMultiplier == 1) round(euxScoreValue) else 0
            } else {
                // nous sommes dedans
                nousTotalValue = 0
                // max pour gèrer le cas du capot inversé
                euxTotalValue = round(max(euxScoreValue,162)) + contractValue
            }
        } else {
            // si score 80 la partie est perdu (l'adversaire à 82)
            if (euxScoreValue > 80 && euxScoreValue >= contract) {
                // Partie faite => à arrondir
                euxTotalValue = contractValue + round(euxScoreValue)
                nousTotalValue = if (coincheMultiplier == 1) round(nousScoreValue) else 0
            } else {
                // eux sont dedans
                euxTotalValue = 0
                // max pour gèrer le cas du capot inversé
                nousTotalValue = round(max(nousScoreValue,162)) + contractValue
            }
        }

        // Belote
        if (nousBelote.isChecked) {
            nousTotalValue += 20
        }
        if (euxBelote.isChecked) {
            euxTotalValue += 20
        }
        // 0 sans être capot ?
        // Partir en étant capot ? (capot inversé)

        nousTotal.text = nousTotalValue.toString().toEditable()
        euxTotal.text = euxTotalValue.toString().toEditable()
    }

    private fun round(scoreValue: Int): Int {
        val unit = scoreValue % 10
        var diz = scoreValue - unit
        if (unit < 5) {
            return diz
        } else {
            return diz + 10
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manche)

        euxScore.text = "0".toEditable()
        nousScore.text = "0".toEditable()
        euxTotal.text = "0"
        nousTotal.text = "0"

        couleur_coeur.setOnClickListener {
            couleur_coeur.isChecked = true
            couleur_pique.isChecked = false
            couleur_carreau.isChecked = false
            couleur_trefle.isChecked = false
            couleur = "coeur"
        }
        couleur_pique.setOnClickListener {
            couleur_coeur.isChecked = false
            couleur_pique.isChecked = true
            couleur_carreau.isChecked = false
            couleur_trefle.isChecked = false
            couleur = "pique"
        }
        couleur_carreau.setOnClickListener {
            couleur_coeur.isChecked = false
            couleur_pique.isChecked = false
            couleur_carreau.isChecked = true
            couleur_trefle.isChecked = false
            couleur = "carreau"
        }
        couleur_trefle.setOnClickListener {
            couleur_coeur.isChecked = false
            couleur_pique.isChecked = false
            couleur_carreau.isChecked = false
            couleur_trefle.isChecked = true
            couleur = "trefle"
        }

        euxButton.setOnClickListener {
            euxButton.isChecked = true
            nousButton.isChecked = false
            computeScores()
        }
        nousButton.setOnClickListener {
            euxButton.isChecked = false
            nousButton.isChecked = true
            computeScores()
        }
        euxBelote.setOnClickListener {
            nousBelote.isChecked = false
            computeScores()
        }
        nousBelote.setOnClickListener {
            euxBelote.isChecked = false
            computeScores()
        }

        coincheSans.setOnClickListener {
            coincheSans.isChecked = true
            coincheAvec.isChecked = false
            coincheSurcoinche.isChecked = false
            coincheMultiplier = 1
            computeScores()
        }
        coincheAvec.setOnClickListener {
            coincheSans.isChecked = false
            coincheAvec.isChecked = true
            coincheSurcoinche.isChecked = false
            coincheMultiplier = 2
            computeScores()
        }
        coincheSurcoinche.setOnClickListener {
            coincheSans.isChecked = false
            coincheAvec.isChecked = false
            coincheSurcoinche.isChecked = true
            coincheMultiplier = 4
            computeScores()
        }
        fab.setOnClickListener { view ->
            val manche = Manche()
            manche.eux = 42
            manche.nous = 120
            val intent = Intent()
            intent.putExtra("Manche", manche)
            setResult(0, intent)
/*            val filename = "myfile"
            val fileContents = "Hello world!"
            baseContext.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(fileContents.toByteArray())
            }*/
            finish()
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
                computeScores()
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
        euxScore.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                try {
                    val intScore = s.toString().toInt()
                    if (intScore == 0 && score_slider.progress == 164) {
                        euxScore.placeCursorToEnd()
                        return
                    }
                    if (intScore+1 != 164 - score_slider.progress) {
                        score_slider.progress = 164 - (intScore+1)
                    }
                    euxScore.placeCursorToEnd()
                } catch (e: NumberFormatException) {
                    score_slider.progress = 0
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
                    if (intScore == 0 && score_slider.progress == 0) {
                        nousScore.placeCursorToEnd()
                        return
                    }
                    if (intScore + 1 != score_slider.progress) {
                        score_slider.progress = intScore +1
                    }
                    nousScore.placeCursorToEnd()
                } catch (e: NumberFormatException) {
                    score_slider.progress = score_slider.max
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
        val nous_score = score_slider.progress
        println(nous_score)
        if (nous_score == 0) {
            euxScore.text = "Capot".toEditable()
            nousScore.text = "0".toEditable()
        } else if (nous_score == 164) {
            nousScore.text = "Capot".toEditable()
            euxScore.text = "0".toEditable()
        } else {

            nousScore.text = (nous_score - 1).toString().toEditable()
            euxScore.text = (162 - (nous_score - 1)).toString().toEditable()
        }
        computeScores()
    }

    // handy extension methods
    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    fun EditText.placeCursorToEnd() {
        this.setSelection(this.text.length)
    }

}
