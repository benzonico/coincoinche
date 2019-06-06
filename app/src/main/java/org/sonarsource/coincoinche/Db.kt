package org.sonarsource.coincoinche

import com.google.firebase.database.FirebaseDatabase

val database = FirebaseDatabase.getInstance()
val gamesRef = database.getReference("games")
