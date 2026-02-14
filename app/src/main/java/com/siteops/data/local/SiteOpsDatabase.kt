package com.siteops.data.local

import androidx.room.*
import com.siteops.data.model.Project
import com.siteops.data.model.SiteVisit
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY createdAt DESC")
    fun getAllProjects(): Flow<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project): Long

    @Delete
    suspend fun deleteProject(project: Project)
}

@Dao
interface SiteVisitDao {
    @Query("SELECT * FROM site_visits WHERE projectId = :projectId ORDER BY date DESC")
    fun getVisitsForProject(projectId: Long): Flow<List<SiteVisit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(visit: SiteVisit): Long

    @Update
    suspend fun updateVisit(visit: SiteVisit)

    @Delete
    suspend fun deleteVisit(visit: SiteVisit)
}

@Database(entities = [Project::class, SiteVisit::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SiteOpsDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun siteVisitDao(): SiteVisitDao
}

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String = value.joinToString(",")

    @TypeConverter
    fun toStringList(value: String): List<String> = if (value.isEmpty()) emptyList() else value.split(",")
}
