package org.sonarsource.coincoinche


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.partie_row_layout.view.*
import kotlinx.android.synthetic.main.activity_partie.*
import kotlinx.android.synthetic.main.activity_partie.view.*
import kotlinx.android.synthetic.main.partie_row_layout.view.hyphen

class MancheListAdapter(private val context: Context, private val dataSource: List<Manche>) : BaseAdapter() {

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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.partie_row_layout, parent, false)
        rowView.scoreMancheEux.text = ""+dataSource[position].eux
        rowView.scoreMancheNous.text = ""+dataSource[position].nous
        rowView.hyphen.text = " - "
        rowView.labelMancheEux.text = "Eux"
        rowView.labelMancheNous.text = "Nous"

        return rowView
    }

}