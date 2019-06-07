package org.sonarsource.coincoinche

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.game_row_layout.view.*
import kotlinx.android.synthetic.main.game_row_layout.view.hyphen
import kotlinx.android.synthetic.main.partie_row_layout.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PartiesListAdapter(private val context: Context, private val dataSource: ArrayList<Game>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun refresh() {
        this.notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.game_row_layout, parent, false)
        val game = dataSource[position]
        rowView.scoreEux.text = dataSource[position].eux.toString()
        rowView.scoreNous.text = dataSource[position].nous.toString()
        rowView.hyphen.text = " - "
        rowView.labelEux.text = "Eux"
        rowView.labelNous.text = "Nous"
        val formatter = SimpleDateFormat("dd/MM")
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis(dataSource[position].date)
        rowView.dateGame.text = formatter.format(calendar.getTime())

        rowView.deleteGame.setOnClickListener {
            val removedGame = dataSource.removeAt(position)
            deleteGame(removedGame, context)
            refresh()
        }

        if (game.eux > game.nous) {
            rowView.imageGameNous.setImageResource(R.drawable.ic_looser_40dp)
            rowView.imageGameEux.setImageResource(R.drawable.ic_star_yellow_40dp)
        }
        else {
            rowView.imageGameEux.setImageResource(R.drawable.ic_looser_40dp)
            rowView.imageGameNous.setImageResource(R.drawable.ic_star_yellow_40dp)
        }

        return rowView
    }
}