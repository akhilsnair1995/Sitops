package com.siteops.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val clientName: String,
    val location: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "site_visits",
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId")]
)
data class SiteVisit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val title: String,
    val date: Long = System.currentTimeMillis(),
    val notes: String = "",
    val photoUris: List<String> = emptyList(), // Store as JSON or comma-separated string via TypeConverter
    val pdfUri: String? = null,
    val markupsJson: String? = null // Store coordinate-based markups as JSON
)
