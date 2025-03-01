package ru.download.prepodavatel_online

import com.google.firebase.database.DataSnapshot

fun DataSnapshot.getCardModel(): CardData =
    this.getValue(CardData::class.java)?:CardData()