package org.sonarsource.coincoinche

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONTokener
import java.io.File
import java.io.FileNotFoundException
import java.util.*
import kotlin.collections.ArrayList

const val COEUR = 0
const val PIQUE = 1
const val CARREAU = 2
const val TREFLE = 3
private var uniqueID: String? = null
private const val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"
@Synchronized
fun id(context: Context): String {
    if (uniqueID == null) {
        val sharedPrefs = context.getSharedPreferences(PREF_UNIQUE_ID, Context.MODE_PRIVATE)
        uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null)
        if (uniqueID == null) {
            uniqueID = UUID.randomUUID().toString()
            val editor = sharedPrefs.edit()
            editor.putString(PREF_UNIQUE_ID, uniqueID)
            editor.commit()
        }
    }
    return uniqueID as String
}

private var saveOnlineEnabled: Boolean = false
private val SAVE_ONLINE_UNIQUE_ID = "SAVE_ONLINE_UNIQUE_ID"
@Synchronized
fun saveOnline(context: Context): Boolean {
    val sharedPrefs = context.getSharedPreferences(SAVE_ONLINE_UNIQUE_ID, Context.MODE_PRIVATE)
    saveOnlineEnabled = sharedPrefs.getBoolean(SAVE_ONLINE_UNIQUE_ID, false)
    return saveOnlineEnabled
}

@Synchronized
fun saveOnline(context: Context, value: Boolean) {
    val sharedPrefs = context.getSharedPreferences(SAVE_ONLINE_UNIQUE_ID, Context.MODE_PRIVATE)
    saveOnlineEnabled = value
    val editor = sharedPrefs.edit()
    editor.putBoolean(SAVE_ONLINE_UNIQUE_ID, saveOnlineEnabled)
    editor.commit()
}

fun readData(context: Context, reloadList: (ArrayList<Game>) -> Unit) {
    if (saveOnline(context)) {
        readOnlineData(context, reloadList)
    } else {
        readLocalData(context, reloadList)
    }
}


private fun readOnlineData(context: Context, reloadList: (ArrayList<Game>) -> Unit) {
    gamesRef.child(id(context)).addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val games = ArrayList<Game>()

            dataSnapshot.children.mapNotNullTo(games) {
                it.getValue<Game>(Game::class.java)
            }

            reloadList(games)
        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
        }
    })
}

fun readLocalData(context: Context, reloadList: (ArrayList<Game>) -> Unit) {
    val games = ArrayList<Game>()
    try {
        val file = File(context.filesDir, "games_data.json")
        val contents = file.readText()
        val jsonArray = JSONArray(JSONTokener(contents))
        for (i in 0 until jsonArray.length()) {
            val jsonGame = jsonArray.getJSONObject(i)
            games.add(Game.fromJson(jsonGame))
        }
    } catch (e: FileNotFoundException) {

    }

    reloadList(games)
}

fun persistGames(games: ArrayList<Game>, context: Context) {
    if (saveOnline(context)) {
        persistOnlineGames(games, context)
    } else {
        persistLocalGames(games, context)
    }
}

private fun persistOnlineGames(games: ArrayList<Game>, context: Context) {
    val path = id(context)
    gamesRef.child(path).removeValue()

    games.map { g ->
        gamesRef.child(path).child("" + g.date).setValue(g)
    }
}

fun persistLocalGames(games: ArrayList<Game>, context: Context) {
    val fileContents = JSONArray(games.map { g -> g.toJson() }).toString()
    context.openFileOutput("games_data.json", Context.MODE_PRIVATE).use {
        it.write(fileContents.toByteArray())
    }
}

fun updateAGame(game: Game, context: Context) {
    // update score to be safe before save
    game.updateScore()

    readData(context, updateCallback(game, context))
}

fun updateCallback(game: Game, context: Context): (ArrayList<Game>) -> Unit {
    return fun(games: ArrayList<Game>) {
        val persistedGame = games.firstOrNull { g -> g.date == game.date }
        if (persistedGame == null) {
            games.add(0, game)
        } else {
            games[games.indexOf(persistedGame)] = game
        }
        persistGames(games, context)
    }
}

fun deleteGame(removedGame: Game, context: Context) {
    readData(context, deleteCallback(removedGame, context))
}

fun deleteCallback(removedGame: Game, context: Context): (ArrayList<Game>) -> Unit {
    return fun(games: ArrayList<Game>) {
        val persistedGame = games.firstOrNull { g -> g.date == removedGame.date }
        if (persistedGame != null) {
            games.remove(persistedGame)
            persistGames(games, context)
        }
    }
}