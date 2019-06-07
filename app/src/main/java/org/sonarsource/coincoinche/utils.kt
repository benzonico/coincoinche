package org.sonarsource.coincoinche

import android.content.Context
import org.json.JSONArray
import org.json.JSONTokener
import java.io.File
import java.io.FileNotFoundException
import java.util.*
import kotlin.collections.ArrayList

private var uniqueID: String? = null
private val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"
@Synchronized
fun id(context: Context): String {
    if (uniqueID == null) {
        val sharedPrefs = context.getSharedPreferences(
            PREF_UNIQUE_ID, Context.MODE_PRIVATE
        )
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

fun readData(context: Context):ArrayList<Game> {
    val games = ArrayList<Game>()
    try {
        val file = File(context.filesDir, "games_data.json")
        val contents = file.readText()
        val jsonArray = JSONArray(JSONTokener(contents))
        for (i in 0 until jsonArray.length()) {
            val jsonGame = jsonArray.getJSONObject(i)
            games.add(Game.fromJson(jsonGame))
        }
    }
    catch(e: FileNotFoundException){

    }
    return games
}

fun persistGames(games:ArrayList<Game>, context: Context) {
    val fileContents = JSONArray(games.map { g -> g.toJson() }).toString()
    context.openFileOutput("games_data.json", Context.MODE_PRIVATE).use {
        it.write(fileContents.toByteArray())
    }
}

fun updateAGame(game: Game, context: Context) {
    // update score to be safe before save
    game.updateScore()
    val games = readData(context)
    val persistedGame = games.firstOrNull { g -> g.date == game.date }
    if(persistedGame == null) {
        games.add(0, game)
    } else {
        games[games.indexOf(persistedGame)] = game
    }
    persistGames(games, context)
}

fun deleteGame(removedGame: Game, context: Context) {
    val games = readData(context)
    val persistedGame = games.firstOrNull { g -> g.date == removedGame.date }
    if(persistedGame != null) {
        games.remove(persistedGame)
        persistGames(games, context)
    }
}
