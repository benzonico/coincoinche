package org.sonarsource.coincoinche

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val dataFilename = "games_data.json"
    private lateinit var listView: ListView
    private lateinit var adapter: PartiesListAdapter
    var games = ArrayList<Game>()

    override fun onResume() {
        super.onResume()
        readData(baseContext, ::reloadList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // initialize games list
        readData(baseContext, ::reloadList)

        fab.setOnClickListener { view ->
            startPartieActivity(Game())
        }

        listView = findViewById(R.id.parties_list)
        listView.setOnItemClickListener { parent, view, position, id ->
            startPartieActivity(games.get(position))
        }

        adapter = PartiesListAdapter(this, games)
        listView.adapter = adapter
    }

    private fun startPartieActivity(game: Game) {
        val intent = Intent(this, PartieActivity::class.java).apply {}
        intent.putExtra("Game", game)
        startActivityForResult(intent, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java).apply {}
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val mygame = data?.getParcelableExtra<Game>("Game")
        if (mygame != null) {
            if (!games.isEmpty()) {
                val first = games.filter { g -> g.date == mygame.date }.firstOrNull()
                if (first != null) {
                    games.remove(first)
                }
            }
            games.add(0, mygame)
            persistGames(games, baseContext)
            adapter.notifyDataSetChanged()
        }
    }

    private fun reloadList(newGames: ArrayList<Game>) {
        games.clear()
        games.addAll(newGames)

        try {
            adapter.notifyDataSetChanged()
        } catch (e: UninitializedPropertyAccessException) {

        }
    }
}
