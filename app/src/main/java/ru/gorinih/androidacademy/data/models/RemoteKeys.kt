package ru.gorinih.androidacademy.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_db")
    val idBase: Int = 0,
    val id: Int = 0,
    val prevKey: Int? = null,
    val nextKey: Int? = null
)