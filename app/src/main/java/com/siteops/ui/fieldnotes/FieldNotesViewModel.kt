package com.siteops.ui.fieldnotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siteops.data.local.ProjectDao
import com.siteops.data.local.SiteVisitDao
import com.siteops.data.model.Project
import com.siteops.data.model.SiteVisit
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.siteops.SiteOpsApplication

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.compose.ui.geometry.Offset

class FieldNotesViewModel(
    private val projectDao: ProjectDao,
    private val siteVisitDao: SiteVisitDao
) : ViewModel() {

    private val gson = Gson()

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val db = SiteOpsApplication.database
                return FieldNotesViewModel(db.projectDao(), db.siteVisitDao()) as T
            }
        }
    }

    val projects: StateFlow<List<Project>> = projectDao.getAllProjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addProject(name: String, client: String, location: String) {
        viewModelScope.launch {
            projectDao.insertProject(Project(name = name, clientName = client, location = location))
        }
    }

    fun getVisits(projectId: Long): StateFlow<List<SiteVisit>> {
        return siteVisitDao.getVisitsForProject(projectId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun getVisitById(visitId: Long): StateFlow<SiteVisit?> {
        return siteVisitDao.getVisitById(visitId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    }

    fun addSiteVisit(projectId: Long, title: String, notes: String, photoUris: List<String>, pdfUri: String?) {
        viewModelScope.launch {
            siteVisitDao.insertVisit(
                SiteVisit(
                    projectId = projectId,
                    title = title,
                    notes = notes,
                    photoUris = photoUris,
                    pdfUri = pdfUri
                )
            )
        }
    }

    fun updateSiteVisit(visit: SiteVisit) {
        viewModelScope.launch {
            siteVisitDao.updateVisit(visit)
        }
    }

    fun serializeMarkups(markups: List<Offset>): String {
        val list = markups.map { listOf(it.x, it.y) }
        return gson.toJson(list)
    }

    fun deserializeMarkups(json: String?): List<Offset> {
        if (json.isNullOrEmpty()) return emptyList()
        return try {
            val type = object : TypeToken<List<List<Float>>>() {}.type
            val list: List<List<Float>> = gson.fromJson(json, type)
            list.map { Offset(it[0], it[1]) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
