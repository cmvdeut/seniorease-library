package com.maureen.biblitoheek.data

import androidx.room.*

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item): Long

    @Query("SELECT * FROM items ORDER BY title ASC")
    suspend fun getAllSortedByTitle(): List<Item>

    @Query("SELECT * FROM items ORDER BY authorOrArtist ASC")
    suspend fun getAllSortedByAuthorOrArtist(): List<Item>

    @Query("SELECT * FROM items ORDER BY id DESC")
    suspend fun getAllSortedByNewest(): List<Item>

    @Query("SELECT * FROM items WHERE code = :code LIMIT 1")
    suspend fun getByCode(code: String): Item?

    @Query("SELECT * FROM items WHERE type = :type")
    suspend fun getByType(type: String): List<Item>

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)
} 