package ru.gorinih.androidacademy.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val id: Int = 0,
    val prevKey: Int? = null,
    val nextKey: Int? = null
)