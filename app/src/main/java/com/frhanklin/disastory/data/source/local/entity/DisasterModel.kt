package com.frhanklin.disastory.data.source.local.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull


@Suppress("DEPRECATED_ANNOTATION")
@SuppressLint("KotlinNullnessAnnotation")
@Entity(tableName = "disaster")
@Parcelize
data class DisasterModel (

    @PrimaryKey
    @ColumnInfo("key")
    @NotNull
    var key: String,

    @ColumnInfo("created_at")
    @NotNull
    var createdAt: String,

    @ColumnInfo("type")
    @NotNull
    var type: String,

    @ColumnInfo("image")
    @NotNull
    var image: String,

    @ColumnInfo("title")
    @NotNull
    var title: String,

    @ColumnInfo("text")
    @NotNull
    val text: String,

    @ColumnInfo("region_code")
    @NotNull
    var regionCode: String,

    @ColumnInfo("lat")
    @NotNull
    var latitude: Double,

    @ColumnInfo("lng")
    @NotNull
    var longitude: Double,


    @ColumnInfo("last_updated")
    @NotNull
    val lastUpdated: String

): Parcelable