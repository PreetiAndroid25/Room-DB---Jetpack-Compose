package com.example.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "tbl_note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @SerialName("id")
    val id: Int = 0,
    @SerialName("noteTitle")
    val noteTitle: String = "",
    @SerialName("noteDescription")
    val noteDescription: String = ""
)
