package com.flyfishxu.onionote.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {
    @Insert
    fun insertNote(vararg note: Note)

    @Query("select id from notes order by id")
    fun getLatest(): List<Int>

    @Query("select * from notes")
    fun getAll(): List<Note>

    @Query("select * from notes where id = :noteId")
    fun getNoteById(noteId: Int): Note

    @Update
    fun updateNote(vararg note: Note)

    @Delete()
    fun deleteNote(vararg note: Note)
}