package org.sonarsource.coincoinche

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.partie_row_layout.view.*
import kotlinx.android.synthetic.main.partie_row_layout.view.hyphen

class MancheListAdapter(private val context: Context, private val game:Game) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val manches = game.manches

    override fun getCount(): Int {
        return manches.size
    }

    override fun getItem(position: Int): Any {
        return manches[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.partie_row_layout, parent, false)
        val manche = manches[position]
        rowView.scoreMancheEux.text = ""+ manche.euxScore()
        rowView.scoreMancheNous.text = ""+ manche.nousScore()
        rowView.hyphen.text = " - "
        rowView.labelMancheEux.text = "Eux"
        rowView.labelMancheNous.text = "Nous"

        rowView.deleteManche.setOnClickListener {
            manches.removeAt(position)
            game.updateScore()
            refresh()
        }
        if (manche.eux > manche.nous) {
//            rowView.imageMancheNous.setImageResource(R.drawable.ic_looser_40dp)
//            rowView.imageMancheEux.setImageResource(R.drawable.ic_star_yellow_40dp)
            rowView.imageMancheNous.setImageResource(R.drawable.ic_radio_button_unchecked_red_40dp)
            rowView.imageMancheEux.setImageResource(R.drawable.ic_radio_button_checked_green_40dp)
        }
        else {
//            rowView.imageMancheEux.setImageResource(R.drawable.ic_looser_40dp)
//            rowView.imageMancheNous.setImageResource(R.drawable.ic_star_yellow_40dp)
            rowView.imageMancheEux.setImageResource(R.drawable.ic_radio_button_unchecked_red_40dp)
            rowView.imageMancheNous.setImageResource(R.drawable.ic_radio_button_checked_green_40dp)
        }


        return rowView
    }

    fun refresh() {
        this.notifyDataSetChanged()
        updateAGame(game, context)
    }

}