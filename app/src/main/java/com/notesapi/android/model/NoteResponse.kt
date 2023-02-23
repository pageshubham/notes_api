package com.notesapi.android.model

data class NoteResponse(
    val __v: Int,
    val _id: String,
    val title: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String,
    val userId: String
)
