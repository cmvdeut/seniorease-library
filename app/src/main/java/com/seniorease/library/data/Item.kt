package com.seniorease.library.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "boek" of "muziek"
    val title: String,
    val authorOrArtist: String,
    val code: String, // ISBN of EAN
    val isReadOrListened: Boolean = false,
    val inPossession: Boolean = false,
    val tbr: Boolean = false, // To Be Read - te lezen
    val medium: String? = null, // "cd", "lp" of null
    val language: String? = null, // "NL", "EN", of vrije tekst
    val coverUrl: String? = null, // URL naar cover/afbeelding
    val googleSearchUrl: String? = null // URL naar Google zoekresultaten
) 