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

class FieldNotesViewModel(
    private val projectDao: ProjectDao,
    private val siteVisitDao: SiteVisitDao
) : ViewModel() {

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
}
